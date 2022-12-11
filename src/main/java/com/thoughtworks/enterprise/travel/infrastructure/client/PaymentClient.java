package com.thoughtworks.enterprise.travel.infrastructure.client;


import com.thoughtworks.enterprise.travel.controller.dto.PaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url="http://localhost:9000")
public interface PaymentClient {

    @PostMapping(value="/payment")
    ClientBaseResponse<String> payment(@RequestBody PaymentRequestDto paymentInfo);
}
