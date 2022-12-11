package com.thoughtworks.enterprise.travel.controller;

import com.thoughtworks.enterprise.travel.controller.dto.ApiResponse;
import com.thoughtworks.enterprise.travel.controller.dto.PaymentRequestDto;
import com.thoughtworks.enterprise.travel.controller.dto.PaymentResponseDto;
import com.thoughtworks.enterprise.travel.service.TravelServiceFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/travel-orders/{oid}")
public class TravelController {
    private final TravelServiceFeeService travelServiceFeeService;

    @PostMapping("service-fee-request/confirmation")
    public ApiResponse<PaymentResponseDto> paymentServiceFee(@PathVariable("oid") Long orderId,
            @RequestBody PaymentRequestDto paymentInfo) {
        return ApiResponse.success(travelServiceFeeService.paymentServiceFee(orderId, paymentInfo));
    }

    @PostMapping("service-fee-invoice-request")
    public ApiResponse<Boolean> serviceFeeInvoiceRequest(@PathVariable("oid") Long orderId) {
        return ApiResponse.success(travelServiceFeeService.serviceFeeInvoiceRequest(orderId));
    }
}
