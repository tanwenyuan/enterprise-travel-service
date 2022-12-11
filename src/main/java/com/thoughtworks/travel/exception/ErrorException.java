package com.thoughtworks.travel.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private final String errorCode;

    public ErrorException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
