package com.thoughtworks.travel.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SUCCESS(200, "请求成功"),
    FAILED(500, "请求失败"),
    REQUEST_THIRD_SYSTEM_TIMEOUT(408, "调用三方接口异常,请稍后重试"),
    TRANSACTION_NOT_PAID(500, "当前交易未支付"),
    ;

    private final int code;
    private final String message;

}
