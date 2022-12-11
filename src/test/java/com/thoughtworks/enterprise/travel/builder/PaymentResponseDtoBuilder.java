package com.thoughtworks.enterprise.travel.builder;

import com.thoughtworks.enterprise.travel.controller.dto.PaymentResponseDto;
import com.thoughtworks.enterprise.travel.enums.AccountType;
import com.thoughtworks.enterprise.travel.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentResponseDtoBuilder {

    private PaymentResponseDto paymentResponseDto;

    private PaymentResponseDtoBuilder() {
    }

    public static PaymentResponseDtoBuilder withDefault() {
        PaymentResponseDtoBuilder builder = new PaymentResponseDtoBuilder();
        builder.paymentResponseDto = PaymentResponseDto.builder()
                .accountId("123")
                .accountType(AccountType.UNION_PAY)
                .amount(BigDecimal.valueOf(1000))
                .paymentStatus(PaymentStatus.PAID)
                .build();
        return builder;
    }

    public PaymentResponseDto build() {
        return paymentResponseDto;
    }

}
