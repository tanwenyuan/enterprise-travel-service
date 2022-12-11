package com.thoughtworks.travel.controller;

import com.thoughtworks.travel.basetest.ApplicationTestBase;
import com.thoughtworks.travel.builder.PaymentRequestDtoBuilder;
import com.thoughtworks.travel.builder.PaymentResponseDtoBuilder;
import com.thoughtworks.travel.enums.AccountType;
import com.thoughtworks.travel.enums.PaymentStatus;
import com.thoughtworks.travel.fixture.TravelServiceFeeServiceFixture;
import com.thoughtworks.travel.infrastructure.utils.JsonUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class TravelControllerTest extends ApplicationTestBase {

    //工序1
    @Test
    @SneakyThrows
    void should_return_200_when_succeed_when_payment_for_service_fee() {
        //given & when
        var paymentRequestDto = PaymentRequestDtoBuilder.withDefault().build();
        var paymentResponseDto = PaymentResponseDtoBuilder.withDefault().build();

        TravelServiceFeeServiceFixture.paymentServiceFee(paymentResponseDto);

        var mvcResult = mockMvc.perform(post("/travel-orders/123/service-fee-request/confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.marshal(paymentRequestDto)))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.paymentStatus", is(PaymentStatus.PAID.name())))
                .andExpect(jsonPath("$.data.accountType", is(AccountType.UNION_PAY.name())))
                .andExpect(jsonPath("$.data.amount", is(1000)))
                .andExpect(jsonPath("$.data.accountId", is("123")))
                .andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    //工序1
    @Test
    @SneakyThrows
    void should_return_payment_error_when_payment_service_is_unavailable() {
        //given & when
        var paymentRequestDto = PaymentRequestDtoBuilder.withDefault().build();

        TravelServiceFeeServiceFixture.paymentServiceFeeException();
        MvcResult mvcResult = mockMvc.perform(post("/travel-orders/123/service-fee-request/confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.marshal(paymentRequestDto)))
                .andReturn();

        //then
        assertEquals(408, mvcResult.getResponse().getStatus());
    }

    //工序1
    @Test
    void should_return_insufficient_fee_error_when_fail_payment() throws Exception {
        //given & when
        var paymentRequestDto = PaymentRequestDtoBuilder.withDefault().build();

        TravelServiceFeeServiceFixture.paymentInsufficientAccountBalanceException();
        var mvcResult = mockMvc.perform(post("/travel-orders/123/service-fee-request/confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.marshal(paymentRequestDto)))
                .andExpect(jsonPath("$.code", is(500)))
                .andExpect(jsonPath("$.errorCode", is("INSUFFICIENT_ACCOUNT_BALANCE")))
                .andExpect(jsonPath("$.message", is("账户余额不足")))
                .andReturn();

        //then
        assertEquals(500, mvcResult.getResponse().getStatus());
    }

    //工序1
    @Test
    @SneakyThrows
    void should_return_not_exist_account_info_when_fail_payment() {
        //given & when
        var paymentRequestDto = PaymentRequestDtoBuilder.withDefault().build();

        TravelServiceFeeServiceFixture.paymentNotExistAccountException();
        var mvcResult = mockMvc.perform(post("/travel-orders/123/service-fee-request/confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.marshal(paymentRequestDto)))
                .andExpect(jsonPath("$.code", is(500)))
                .andExpect(jsonPath("$.errorCode", is("NOT_EXIST_ACCOUNT")))
                .andExpect(jsonPath("$.message", is("账号不存在")))
                .andReturn();

        //then
        assertEquals(500, mvcResult.getResponse().getStatus());
    }

    //工序1
    @Test
    @SneakyThrows
    void should_return_success_when_succeed_to_request_service_fee_invoice() {
        //given & when
        TravelServiceFeeServiceFixture.serviceFeeInvoiceRequestSuccess();
        var mvcResult = mockMvc.perform(post("/travel-orders/123/service-fee-invoice-request")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data", is(true)))
                .andReturn();

        //then
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    //工序1
    @Test
    @SneakyThrows
    void should_return_error_when_could_not_pay_service_fee() {
        //given & when
        TravelServiceFeeServiceFixture.sendEvaluationRequestException();

        var mvcResult = mockMvc.perform(post("/travel-orders/123/service-fee-invoice-request")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(500)))
                .andExpect(jsonPath("$.errorCode", is("TRANSACTION_NOT_PAID")))
                .andExpect(jsonPath("$.message", is("当前交易未支付")))
                .andReturn();

        //then
        assertEquals(500, mvcResult.getResponse().getStatus());
    }
}
