package com.thoughtworks.enterprise.travel.infrastructure.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.thoughtworks.enterprise.travel.controller.dto.PaymentRequestDto;
import com.thoughtworks.enterprise.travel.infrastructure.utils.JsonUtils;
import com.thoughtworks.enterprise.travel.basetest.ApplicationTestBase;
import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentClientTest extends ApplicationTestBase {

    @Autowired
    private PaymentClient paymentClient;

    private WireMockServer wm;

    @BeforeEach
    void setUp() {
        wm = new WireMockServer(9000);
        wm.start();
    }

    @AfterEach
    void tearDown() {
        wm.stop();
    }

    //工序三
    @Test
    void should_return_success_when_mock_server_return_success() {
        //given & when
        WireMock.configureFor("localhost", 9000);
        stubFor(post(urlMatching("/payment"))
                .willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(JsonUtils.marshal(ClientBaseResponse.<String>builder()
                                .code(200).message("成功")
                                .build()))));

        ClientBaseResponse<String> response = paymentClient.payment(PaymentRequestDto.builder().build());

        //then
        assertEquals(200, response.getCode());
        assertEquals("成功", response.getMessage());
    }

    //工序三
    @Test
    void should_throw_exception_when_mock_server_throw_exception() {
        //given & when
        WireMock.configureFor("localhost", 9000);
        stubFor(post(urlMatching("/payment"))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        //then
        assertThrows(FeignException.class,
                () -> paymentClient.payment(PaymentRequestDto.builder().build()));
    }
}
