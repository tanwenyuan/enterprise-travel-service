package com.thoughtworks.enterprise.travel.controller.dto;

import com.thoughtworks.enterprise.travel.infrastructure.enums.ErrorCode;
import lombok.Builder;
import lombok.Data;

import static com.thoughtworks.enterprise.travel.infrastructure.enums.ErrorCode.SUCCESS;

@Data
@Builder
@SuppressWarnings("unchecked")
public class ApiResponse<T> {
    private int code;
    private T data;
    private String errorCode;
    private String message;
    public static ApiResponse<?> error(ErrorCode errorCode) {
        return ApiResponse.builder().code(errorCode.getCode())
                .errorCode(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static ApiResponse<?> error(String errorCode, String message) {
        return ApiResponse.builder().code(ErrorCode.FAILED.getCode())
                .errorCode(errorCode)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return (ApiResponse<T>)ApiResponse.builder().code(SUCCESS.getCode()).message(SUCCESS.getMessage()).data(data)
                .build();
    }
}
