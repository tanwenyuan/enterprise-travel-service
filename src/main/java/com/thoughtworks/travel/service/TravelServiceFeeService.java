package com.thoughtworks.travel.service;

import com.thoughtworks.travel.enums.PaymentStatus;
import com.thoughtworks.travel.controller.dto.PaymentRequestDto;
import com.thoughtworks.travel.controller.dto.PaymentResponseDto;
import com.thoughtworks.travel.exception.ErrorCodeException;
import com.thoughtworks.travel.exception.ErrorException;
import com.thoughtworks.travel.infrastructure.client.ClientBaseResponse;
import com.thoughtworks.travel.infrastructure.client.PaymentClient;
import com.thoughtworks.travel.infrastructure.enums.ErrorCode;
import com.thoughtworks.travel.infrastructure.repository.ServiceFeeRepository;
import com.thoughtworks.travel.infrastructure.repository.entity.ServiceFee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelServiceFeeService {
    private final PaymentClient paymentClient;
    private final ServiceFeeRepository serviceFeeRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.kafka.travel-invoice-topic}")
    private String travelInvoiceTopic;
    public PaymentResponseDto paymentServiceFee(Long orderId, PaymentRequestDto paymentInfo) {
        ServiceFee serviceFee = serviceFeeRepository.findServiceFeeByContractId(orderId);
        ClientBaseResponse<String> response = paymentClient.payment(paymentInfo);
        if (!response.isSuccess()) {
            throw new ErrorException(response.getErrorCode(), response.getMessage());
        }

        serviceFee.setPaymentStatus(PaymentStatus.PAID);
        serviceFee.setPayTime(LocalDateTime.now());
        serviceFeeRepository.save(serviceFee);

        return PaymentResponseDto.builder()
                .accountId(paymentInfo.getAccountId())
                .accountType(paymentInfo.getAccountType())
                .paymentStatus(serviceFee.getPaymentStatus())
                .build();
    }

    public Boolean serviceFeeInvoiceRequest(Long orderId) {
        ServiceFee fee = serviceFeeRepository.findServiceFeeByContractId(orderId);
        if (!Objects.equals(fee.getPaymentStatus(), PaymentStatus.PAID)) {
            throw new ErrorCodeException(ErrorCode.TRANSACTION_NOT_PAID);
        }
        kafkaTemplate.send(travelInvoiceTopic, orderId.toString());
        return true;
    }
}
