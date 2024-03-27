package com.noah.backend.domain.groupaccount.repository.custom;

import com.noah.backend.domain.groupaccount.dto.responseDto.GroupAccountInfoDto;
import com.noah.backend.domain.memberTravel.dto.Response.GetTravelListResDto;

import java.util.List;
import java.util.Optional;

public interface GroupAccountRepositoryCustom {

    Optional<GroupAccountInfoDto> getGroupAccountInfo(Long groupAccountId);

    Optional<List<GroupAccountInfoDto>> getGroupAccountListByMemberId(Long memberId);

    Optional<List<Long>> getGroupAccountIdsByMemberId(Long memberId);
}