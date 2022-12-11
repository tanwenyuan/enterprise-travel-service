package com.thoughtworks.enterprise.travel.infrastructure.repository;

import com.thoughtworks.enterprise.travel.infrastructure.repository.entity.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {
    ServiceFee findServiceFeeByContractId(Long id);

    ServiceFee save(ServiceFee serviceFee);
}
