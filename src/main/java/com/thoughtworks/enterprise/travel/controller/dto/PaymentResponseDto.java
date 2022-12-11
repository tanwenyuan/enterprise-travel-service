package com.thoughtworks.enterprise.travel.controller.dto;

import com.thoughtworks.enterprise.travel.enums.AccountType;
import com.thoughtworks.enterprise.travel.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponseDto {
    private String accountId;
    private BigDecimal amount;
    private AccountType accountType;
    private PaymentStatus paymentStatus;
}
