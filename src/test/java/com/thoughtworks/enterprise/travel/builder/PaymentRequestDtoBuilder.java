package com.thoughtworks.enterprise.travel.builder;

import com.thoughtworks.enterprise.travel.controller.dto.PaymentRequestDto;
import com.thoughtworks.enterprise.travel.enums.AccountType;

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
