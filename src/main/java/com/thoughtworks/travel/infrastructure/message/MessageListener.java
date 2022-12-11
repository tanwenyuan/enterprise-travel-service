package com.thoughtworks.travel.infrastructure.message;

import com.thoughtworks.travel.service.TravelServiceFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    private final TravelServiceFeeService travelServiceFeeService;

    public MessageListener(TravelServiceFeeService travelServiceFeeService) {
        this.travelServiceFeeService = travelServiceFeeService;
    }

    @KafkaListener(
            topics = "${spring.kafka.travel-invoice-topic}",
            id = "${spring.kafka.travel-invoice-topic}",
            groupId = "${spring.application.name}",
            properties = {
                    "spring.json.use.type.headers=false",
            })
    public void receiveMessage(String message) {
        log.info("consume message: {}", message);
    }
}
