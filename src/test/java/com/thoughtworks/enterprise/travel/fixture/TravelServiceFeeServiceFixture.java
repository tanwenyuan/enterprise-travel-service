package com.thoughtworks.enterprise.travel.fixture;

import com.thoughtworks.enterprise.travel.controller.dto.PaymentResponseDto;
import com.thoughtworks.enterprise.travel.exception.ErrorCodeException;
import com.thoughtworks.enterprise.travel.exception.ErrorException;
import com.thoughtworks.enterprise.travel.service.TravelServiceFeeService;
import com.thoughtworks.enterprise.travel.infrastructure.enums.ErrorCode;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TravelServiceFeeServiceFixture {

    private final static TravelServiceFeeService CUSTOMER_TRAVEL_SERVICE = mock(TravelServiceFeeService.class);

    public static void paymentServiceFee(PaymentResponseDto responseDto) {
        when(CUSTOMER_TRAVEL_SERVICE.paymentServiceFee(any(), any())).thenReturn(responseDto);
    }

    public static void paymentServiceFeeException() {
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

        when(CUSTOMER_TRAVEL_SERVICE.paymentServiceFee(any(), any())).thenThrow(
                FeignException.errorStatus("", response));
    }

    public static void paymentInsufficientAccountBalanceException() {
        when(CUSTOMER_TRAVEL_SERVICE.paymentServiceFee(any(), any())).thenThrow(
                new ErrorException("INSUFFICIENT_ACCOUNT_BALANCE", "??????????????????"));
    }

    public static void paymentNotExistAccountException() {
        when(CUSTOMER_TRAVEL_SERVICE.paymentServiceFee(any(), any())).thenThrow(
                new ErrorException("NOT_EXIST_ACCOUNT", "???????????????"));
    }

    public static void serviceFeeInvoiceRequestSuccess() {
        when(CUSTOMER_TRAVEL_SERVICE.serviceFeeInvoiceRequest(any())).thenReturn(true);
    }

    public static void sendEvaluationRequestException() {
        when(CUSTOMER_TRAVEL_SERVICE.serviceFeeInvoiceRequest(any()))
                .thenThrow(new ErrorCodeException(ErrorCode.TRANSACTION_NOT_PAID));
    }

    @Bean
    @Primary
    @Qualifier("mock")
    public TravelServiceFeeService mockTravelServiceFeeService() {
        return CUSTOMER_TRAVEL_SERVICE;
    }

}
