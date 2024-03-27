package com.noah.backend.global.exception.bank;

import com.noah.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class A1016Exception extends RuntimeException{

    private ErrorCode errorCode;

    public A1016Exception(){
        this.errorCode = ErrorCode.EXCEEDED_TRANSFER_LIMIT_ONCE;
    }
}
