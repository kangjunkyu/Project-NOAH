package com.noah.backend.domain.account.controller;

import com.noah.backend.domain.account.dto.requestDto.AccountPostDto;
import com.noah.backend.domain.account.repository.AccountRepository;
import com.noah.backend.domain.account.service.AccountService;
import com.noah.backend.global.format.code.ApiResponse;
import com.noah.backend.global.format.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account 컨트롤러", description = "Account Controller API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final ApiResponse response;
    private final AccountService accountService;
//    private final MemberService memberService;

    @Operation(summary = "계좌 생성", description = "계좌 생성")
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountPostDto accountPostDto) {
        Long accountId = accountService.createAccount(accountPostDto);
        return response.success(ResponseCode.ACCOUNT_CREATED, accountId);
    }

    @Operation(summary = "멤버별 계좌 조회", description = "멤버별 계좌 조회")
    @GetMapping("/my/{memberId}")
    public ResponseEntity<?> getMyAccountList(@PathVariable Long memberId){

        return response.success(ResponseCode.ACCOUNT_LIST_FETCHED, accountService.getMyAccountList(memberId));
    }


}
