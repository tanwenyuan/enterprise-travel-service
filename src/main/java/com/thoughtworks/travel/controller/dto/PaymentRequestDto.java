package com.thoughtworks.travel.controller.dto;

import com.thoughtworks.travel.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequestDto {
    private String accountId;
    private BigDecimal amount;
    private AccountType accountType;
}
