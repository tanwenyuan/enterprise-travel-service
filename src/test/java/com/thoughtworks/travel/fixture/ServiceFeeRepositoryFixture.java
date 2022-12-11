package com.thoughtworks.travel.fixture;

import com.thoughtworks.travel.enums.PaymentStatus;
import com.thoughtworks.travel.infrastructure.repository.ServiceFeeRepository;
import com.thoughtworks.travel.infrastructure.repository.entity.ServiceFee;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class ServiceFeeRepositoryFixture {

    private final static ServiceFeeRepository REPOSITORY = mock(ServiceFeeRepository.class);

    public static void save() {
        when(REPOSITORY.save(any())).thenReturn(
                ServiceFee.builder().paymentStatus(PaymentStatus.PAID).build());
    }

    public static void findServiceFee(Long id) {
        when(REPOSITORY.findServiceFeeByContractId(id)).thenReturn(
                ServiceFee.builder()
                        .contractId(2L)
                        .amount(BigDecimal.valueOf(1000))
                        .payTime(LocalDateTime.now())
                        .paymentStatus(PaymentStatus.PAID)
                        .build()
        );
    }

    //@Bean
    //@Primary
    //@Qualifier("mock")
    public ServiceFeeRepository mockServiceFeeRepository() {
        return REPOSITORY;
    }

}
