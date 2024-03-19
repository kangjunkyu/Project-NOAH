package com.noah.backend.domain.account.controller;

import com.noah.backend.domain.account.dto.requestDto.AccountUpdateDto;
import com.noah.backend.domain.account.dto.responseDto.AccountInfoDto;
import com.noah.backend.domain.account.entity.Account;
import com.noah.backend.domain.account.service.AccountService;
import com.noah.backend.global.format.code.ApiResponse;
import com.noah.backend.global.format.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account 컨트롤러", description = "Account Controller API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final ApiResponse response;
    private final AccountService accountService;
//    private MemberService memberService;

//    @Operation(summary = "내가 속해있는 계좌 조회", description = "내가 속해있는 계좌 조회")
//    @GetMapping
//    public ResponseEntity<?> getMyAccountList(@Parameter(hidden = true) Authentication authentication) {
//
////        Member member = memberService.findMember(authentication.getName());
////        Long userId = member.getId();
//        return response.success(ResponseCode.ACCOUNT_LIST_FETCHED, accountService.getMyAccountList(userId));
//    }

    @Operation(summary = "계좌정보 조회", description = "단일 계좌정보 조회")
    @GetMapping("/{id}")
//    public ResponseEntity<?> getAccountInfo(@Parameter(hidden = true) Authentication authentication, @PathVariable(name = "id") Long accountId) {
    public ResponseEntity<?> getAccountInfo(@PathVariable(name = "id") Long accountId) {
        AccountInfoDto account = accountService.getAccountInfo(accountId);
        return response.success(ResponseCode.ACCOUNT_INFO_FETCHED, account);
    }

    @Operation(summary = "계좌 목표 수정", description = "납입금, 납입일, 목표금액 수정")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccountInfo(@Parameter(hidden = true) Authentication authentication, @RequestBody AccountUpdateDto accountUpdateDto) {
        Long accountId = accountService.updateAccount(accountUpdateDto);
        return response.success(ResponseCode.ACCOUNT_INFO_UPDATED, accountId);
    }

    @Operation(summary = "계좌 삭제", description = "계좌 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@Parameter(hidden = true) Authentication authentication, @PathVariable(name = "id") Long accountId) {
        accountService.deleteAccount(accountId);
        return response.success(ResponseCode.ACCOUNT_DELETED);
    }


}
