package com.thoughtworks.travel.builder;

import com.thoughtworks.travel.enums.PaymentStatus;
import com.thoughtworks.travel.infrastructure.repository.entity.ServiceFee;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

public class ServiceFeeBuilder {

    private ServiceFee serviceFee;

    private ServiceFeeBuilder() {
    }

    public static ServiceFeeBuilder withDefault(Long id) {
        ServiceFeeBuilder builder = new ServiceFeeBuilder();
        builder.serviceFee = ServiceFee.builder()
                .contractId(id)
                .period(Year.now())
                .amount(BigDecimal.valueOf(1000))
                .payTime(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PAID)
                .build();
        return builder;
    }

    public static ServiceFeeBuilder withPaid(Long id) {
        ServiceFeeBuilder builder = new ServiceFeeBuilder();
        builder.serviceFee = ServiceFee.builder()
                .contractId(id)
                .period(Year.now())
                .amount(BigDecimal.valueOf(1000))
                .payTime(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PAID)
                .build();
        return builder;
    }

    public static ServiceFeeBuilder withUnpaid(Long id) {
        ServiceFeeBuilder builder = new ServiceFeeBuilder();
        builder.serviceFee = ServiceFee.builder()
                .contractId(id)
                .period(Year.now())
                .amount(BigDecimal.valueOf(1000))
                .payTime(LocalDateTime.now())
                .paymentStatus(PaymentStatus.UNPAID)
                .build();
        return builder;
    }

    public ServiceFeeBuilder withPaymentStatus(PaymentStatus paymentStatus) {
        serviceFee.setPaymentStatus(paymentStatus);
        return this;
    }

    public ServiceFee build() {
        return serviceFee;
    }

}
