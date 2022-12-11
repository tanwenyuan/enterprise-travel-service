package com.thoughtworks.enterprise.travel.infrastructure.repository.entity;

import com.thoughtworks.enterprise.travel.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceFee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long contractId;
    private Year period;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime payTime;
}
