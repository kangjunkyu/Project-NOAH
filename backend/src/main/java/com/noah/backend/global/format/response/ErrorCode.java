package com.noah.backend.global.format.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 회원(Member) */
    SIGNUP_FAILED(HttpStatus.BAD_REQUEST, "회원 가입에 실패했습니다."),

    /* 계좌(Account) */
    GROUPACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계좌를 찾을 수 없습니다."),

    /* 여행(Travel) */
    TRAVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "여행을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

}
