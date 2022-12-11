package com.thoughtworks.travel.exception;

import com.thoughtworks.travel.infrastructure.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
