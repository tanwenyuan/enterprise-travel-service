package com.thoughtworks.travel.builder;

import com.thoughtworks.travel.infrastructure.client.ClientBaseResponse;

public class PaymentClientResponseBuilder {

    private ClientBaseResponse<String> response;

    private PaymentClientResponseBuilder() {
    }

    public static PaymentClientResponseBuilder withSuccess() {
        PaymentClientResponseBuilder builder = new PaymentClientResponseBuilder();
        builder.response = ClientBaseResponse.<String>builder()
                .code(200).message("success")
                .build();
        return builder;
    }

    public static PaymentClientResponseBuilder withInsufficientBalance() {
        PaymentClientResponseBuilder builder = new PaymentClientResponseBuilder();
        builder.response = ClientBaseResponse.<String>builder()
                .code(500)
                .errorCode("INSUFFICIENT_ACCOUNT_BALANCE")
                .message("账户余额不足")
                .build();
        return builder;
    }

    public static PaymentClientResponseBuilder withNotExistAccount() {
        PaymentClientResponseBuilder builder = new PaymentClientResponseBuilder();
        builder.response = ClientBaseResponse.<String>builder()
                .code(500)
                .errorCode("NOT_EXIST_ACCOUNT")
                .message("账号不存在")
                .build();
        return builder;
    }

    public ClientBaseResponse<String> build() {
        return response;
    }

}
