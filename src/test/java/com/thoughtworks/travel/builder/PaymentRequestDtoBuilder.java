package com.thoughtworks.travel.builder;

import com.thoughtworks.travel.enums.AccountType;
import com.thoughtworks.travel.controller.dto.PaymentRequestDto;

import java.math.BigDecimal;

public class PaymentRequestDtoBuilder {

    private PaymentRequestDto paymentRequestDto;

    private PaymentRequestDtoBuilder() {
    }

    public static PaymentRequestDtoBuilder withDefault() {
        PaymentRequestDtoBuilder builder = new PaymentRequestDtoBuilder();
        builder.paymentRequestDto = PaymentRequestDto.builder()
                .accountId("123")
                .accountType(AccountType.UNION_PAY)
                .amount(BigDecimal.valueOf(1000))
                .build();
        return builder;
    }

    public PaymentRequestDto build() {
        return paymentRequestDto;
    }

}
