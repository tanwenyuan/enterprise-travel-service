package com.thoughtworks.travel.infrastructure.repository;

import com.thoughtworks.travel.basetest.ApplicationTestBase;
import com.thoughtworks.travel.builder.ServiceFeeBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceFeeRepositoryTest extends ApplicationTestBase {

    @Autowired
    ServiceFeeRepository serviceFeeRepository;

    //工序5
    @Test
    void should_return_service_fee_when_find_by_contract_id() {
        //given & when
        long id = 1L;
        serviceFeeRepository.save(ServiceFeeBuilder.withDefault(id).build());

        var serviceFee = serviceFeeRepository.findServiceFeeByContractId(id);

        //then
        assertEquals(id, serviceFee.getContractId());
        assertEquals(new BigDecimal("1000.00"), serviceFee.getAmount());
    }

    //工序5
    @Test
    void should_save_multi_service_fee_when_call_save_method() {
        //given & when
        long id = 1L;
        long id2 = 2L;
        serviceFeeRepository.save(ServiceFeeBuilder.withDefault(id).build());
        serviceFeeRepository.save(ServiceFeeBuilder.withDefault(id2).build());

        var allRecords = serviceFeeRepository.findAll();

        //then
        assertEquals(2, allRecords.size());
        assertEquals(new BigDecimal("1000.00"), allRecords.get(0).getAmount());
    }
}
