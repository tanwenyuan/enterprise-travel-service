package com.thoughtworks.travel.service;

import com.thoughtworks.travel.basetest.ApplicationTestBase;
import com.thoughtworks.travel.builder.PaymentClientResponseBuilder;
import com.thoughtworks.travel.builder.PaymentRequestDtoBuilder;
import com.thoughtworks.travel.builder.ServiceFeeBuilder;
import com.thoughtworks.travel.enums.PaymentStatus;
import com.thoughtworks.travel.exception.ErrorCodeException;
import com.thoughtworks.travel.exception.ErrorException;
import com.thoughtworks.travel.infrastructure.client.PaymentClient;
import com.thoughtworks.travel.infrastructure.enums.ErrorCode;
import com.thoughtworks.travel.infrastructure.repository.ServiceFeeRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
//class TravelServiceFeeServiceTest  {
class TravelServiceFeeServiceTest extends ApplicationTestBase {
    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private TravelServiceFeeService travelServiceFeeService;

    @Mock
    private ServiceFeeRepository serviceFeeRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    // 工序2
    @Test
    void should_payment_service_fee_and_store_payment_result() {
        //given & when
        Long id = 1L;

        when(paymentClient.payment(any())).thenReturn(PaymentClientResponseBuilder.withSuccess().build());
        when(serviceFeeRepository.findServiceFeeByContractId(id)).thenReturn(ServiceFeeBuilder.withUnpaid(1L).build());
        when(serviceFeeRepository.save(any())).thenReturn(ServiceFeeBuilder.withPaid(1L).build());

        var paymentResponseDto = travelServiceFeeService.paymentServiceFee(id,
                PaymentRequestDtoBuilder.withDefault().build());

        //then
        assertEquals(PaymentStatus.PAID, paymentResponseDto.getPaymentStatus());
    }

    // 工序2
    @Test
    void should_payment_service_fee_throw_exception_when_payment_timeout_error() {
        //given & when
        Long id = 1L;
        var request = Request.create(
                Request.HttpMethod.POST,
                "localhost",
                new HashMap<>(),
                Request.Body.empty(),
                new RequestTemplate());
        var response =
                Response.builder()
                        .request(request)
                        .status(HttpStatus.REQUEST_TIMEOUT.value())
                        .build();

        when(serviceFeeRepository.findServiceFeeByContractId(id)).thenReturn(ServiceFeeBuilder.withUnpaid(id).build());
        when(paymentClient.payment(any())).thenThrow(FeignException.errorStatus("", response));
        when(serviceFeeRepository.save(any())).thenReturn(ServiceFeeBuilder.withPaid(id).build());

        FeignException exception = assertThrows(FeignException.class,
                () -> travelServiceFeeService.paymentServiceFee(id, PaymentRequestDtoBuilder.withDefault().build()));

        //then
        assertThat(exception.status()).isEqualTo(HttpStatus.REQUEST_TIMEOUT.value());
    }

    //工序2
    @Test
    void should_throw_insufficient_fee_exception_when_insufficient_balance() {
        //given & when
        Long id = 1L;

        when(serviceFeeRepository.findServiceFeeByContractId(id)).thenReturn(ServiceFeeBuilder.withUnpaid(id).build());
        when(paymentClient.payment(any())).thenReturn(PaymentClientResponseBuilder.withInsufficientBalance().build());

        ErrorException exception = assertThrows(ErrorException.class,
                () -> travelServiceFeeService.paymentServiceFee(id, PaymentRequestDtoBuilder.withDefault().build()));

        //then
        assertEquals("INSUFFICIENT_ACCOUNT_BALANCE", exception.getErrorCode());
        assertEquals("账户余额不足", exception.getMessage());
    }

    //工序2
    @Test
    void should_throw_incorrect_account_info_exception_when_not_exist_account() {
        //given & when
        Long id = 1L;

        when(serviceFeeRepository.findServiceFeeByContractId(id)).thenReturn(ServiceFeeBuilder.withUnpaid(id).build());
        when(paymentClient.payment(any())).thenReturn(PaymentClientResponseBuilder.withNotExistAccount().build());

        ErrorException exception = assertThrows(ErrorException.class,
                () -> travelServiceFeeService.paymentServiceFee(id, PaymentRequestDtoBuilder.withDefault().build()));

        //then
        assertEquals("NOT_EXIST_ACCOUNT", exception.getErrorCode());
        assertEquals("账号不存在", exception.getMessage());
    }

    //工序2
    @Test
    void should_return_true_when_request_sent_successfully() {
        //given & when
        Long id = 1L;
        when(serviceFeeRepository.findServiceFeeByContractId(id)).thenReturn(ServiceFeeBuilder.withPaid(id).build());
        when(kafkaTemplate.send(any(), any())).thenReturn(any());

        var result = travelServiceFeeService.serviceFeeInvoiceRequest(id);

        //then
        assertTrue(result);
    }

    //工序2
    @Test
    void should_throw_unpaid_exception_when_sevice_fee_unpaid() {
        //given & when
        Long id = 1L;
        when(serviceFeeRepository.findServiceFeeByContractId(id)).thenReturn(ServiceFeeBuilder.withUnpaid(id).build());

        ErrorCodeException exception = assertThrows(ErrorCodeException.class, () ->
                travelServiceFeeService.serviceFeeInvoiceRequest(id)
        );

        //then
        assertEquals(ErrorCode.TRANSACTION_NOT_PAID, exception.getErrorCode());
    }
}
