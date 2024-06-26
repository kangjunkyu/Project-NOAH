package com.noah.backend.domain.memberTravel.Service.impl;

import com.noah.backend.domain.account.dto.requestDto.AutoTransferPostDto;
import com.noah.backend.domain.account.entity.Account;
import com.noah.backend.domain.account.repository.AccountRepository;
import com.noah.backend.domain.member.entity.Member;
import com.noah.backend.domain.member.repository.MemberRepository;
import com.noah.backend.domain.memberTravel.Repository.MemberTravelRepository;
import com.noah.backend.domain.memberTravel.Service.MemberTravelService;
import com.noah.backend.domain.memberTravel.dto.Request.MemberTravelInviteDto;
import com.noah.backend.domain.memberTravel.dto.Request.MemberTravelPostDto;
import com.noah.backend.domain.memberTravel.dto.Request.MemberTravelUpdateDto;
import com.noah.backend.domain.memberTravel.entity.MemberTravel;
import com.noah.backend.domain.notification.entity.Notification;
import com.noah.backend.domain.notification.repository.NotificationRepository;
import com.noah.backend.domain.notification.service.NotificationService;
import com.noah.backend.domain.travel.entity.Travel;
import com.noah.backend.domain.travel.repository.TravelRepository;
import com.noah.backend.global.exception.account.AccountNotFoundException;
import com.noah.backend.global.exception.member.MemberNotFoundException;
import com.noah.backend.global.exception.membertravel.MemberTravelAccessException;
import com.noah.backend.global.exception.membertravel.MemberTravelAlreadyExistException;
import com.noah.backend.global.exception.membertravel.MemberTravelAlreadyInvitedException;
import com.noah.backend.global.exception.notification.NotificationSendFailedException;
import com.noah.backend.global.exception.membertravel.MemberTravelNotFoundException;
import com.noah.backend.global.exception.travel.TravelMemberNotFoundException;
import com.noah.backend.global.exception.travel.TravelNotFoundException;
import com.noah.backend.global.exception.travelmember.MemberTravelNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberTravelServiceImpl implements MemberTravelService {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final TravelRepository travelRepository;
    private final MemberTravelRepository memberTravelRepository;
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    @Override
    public Long createMemberTravel(MemberTravelPostDto memberTravelPostDto) {

        Travel travel = travelRepository.findById(memberTravelPostDto.getTravel_id()).orElseThrow(TravelNotFoundException::new);
        Member member = memberRepository.findById(memberTravelPostDto.getMember_id()).orElseThrow(MemberTravelNotFound::new);

        MemberTravel memberTravel =  MemberTravel.builder()
                .payment_amount(memberTravelPostDto.getPayment_amount())
                .member(member)
                .travel(travel)
                .build();

        MemberTravel saveMemberTravel = memberTravelRepository.save(memberTravel);

        return saveMemberTravel.getId();
    }

    @Override
    public Long updateMemberTravel(Long memberTravelId, MemberTravelUpdateDto memberTravelUpdateDto) {
        MemberTravel updateMemberTravel = memberTravelRepository.findById(memberTravelId)
                .orElseThrow(MemberTravelNotFound::new);
        updateMemberTravel.setPayment_amount(memberTravelUpdateDto.getPayment_amount());

        memberTravelRepository.save(updateMemberTravel);

        return updateMemberTravel.getId();
    }

    @Transactional
    @Override
    public Long inviteMember(String email, MemberTravelInviteDto memberTravelInviteDto) {

        /* 접근권한 */
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        MemberTravel mt =  memberTravelRepository.findByTravelIdAndMemberId(member.getId(), memberTravelInviteDto.getTravelId()).orElseThrow(MemberTravelAccessException::new);
        /* ------ */

        Member receiver = memberRepository.findByEmail(memberTravelInviteDto.getEmail()).orElseThrow(MemberNotFoundException::new);

        // 여행에 이미 초대되어있는지 확인
        Notification notify = notificationRepository.findInviteNotification(receiver.getId(), memberTravelInviteDto.getTravelId()).orElse(null);
        if(notify != null){
            throw new MemberTravelAlreadyInvitedException();
        }

        // 여행에 이미 가입되어있는지 확인
        MemberTravel memberTravel = memberTravelRepository.findByTravelIdAndMemberId(receiver.getId(), memberTravelInviteDto.getTravelId()).orElse(null);
        if(memberTravel != null){
            throw new MemberTravelAlreadyExistException();
        }

        // 초대 요청을 보내기 = 알림 보내기
        // 멤버트래블 테이블에 데이터를 저장하는건 요청 받은 사람이 수락하면 저장할 것임
        Travel travel = travelRepository.findById(memberTravelInviteDto.getTravelId()).orElseThrow(TravelMemberNotFoundException::new);

        Notification notification = Notification.builder()
            .receiver(receiver)
            .type(1)
            .travelId(memberTravelInviteDto.getTravelId())
            .travelTitle(travel.getTitle())
            .build();

        Notification savedNotification = notificationRepository.save(notification);

        // 파이어베이스 푸쉬 알림
        String title = "NOAH";
        String body = "[ " + travel.getTitle() + " ] 여행에 초대되었습니다.";
        if(!notificationService.sendNotificationByToken(receiver.getNotificationToken(), title, body)){
            throw new NotificationSendFailedException();
        }

        return savedNotification.getId();
    }


    @Override
    public void deleteResistMember(Long memberTravelId) {

        memberTravelRepository.deleteById(memberTravelId);
    }

    @Transactional
    @Override
    public void setAutoTransfer(String email, AutoTransferPostDto autoTransferPostDto) {

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        MemberTravel memberTravel = memberTravelRepository.findByTravelIdAndMemberId(member.getId(), autoTransferPostDto.getTravelId()).orElseThrow(
            MemberTravelAccessException::new);

        Account account = accountRepository.findById(autoTransferPostDto.getAccountId()).orElseThrow(AccountNotFoundException::new);

        memberTravel.setAccount(account);
        memberTravel.setAutoTransfer(autoTransferPostDto.isAutoActivate());

    }

    @Transactional
    @Override
    public void deleteAutoTransfer(String email, Long travelId) {

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        MemberTravel memberTravel = memberTravelRepository.findByTravelIdAndMemberId(member.getId(), travelId).orElseThrow(
            MemberTravelAccessException::new);

        memberTravel.setAccount(null);

    }

    @Override
    public boolean memberAccessTravel(Long memberId, Long travelId) {
        MemberTravel memberTravel = memberTravelRepository.findByTravelIdAndMemberId(memberId, travelId).orElse(null);
        if(memberTravel == null) throw new MemberTravelAccessException();
        return true;
    }
}
