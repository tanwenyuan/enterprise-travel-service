package com.thoughtworks.enterprise.travel.infrastructure.message;

import com.thoughtworks.enterprise.travel.builder.MessageBuilder;
import com.thoughtworks.enterprise.travel.util.KafkaTestUtil;
import com.thoughtworks.enterprise.travel.basetest.ApplicationTestBase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaMessageTest extends ApplicationTestBase {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DefaultKafkaConsumerFactory<String, String> dictConsumerFactory;

    @SneakyThrows
    @Test
    @DisplayName("should produce value when send message")
    void testProducer() {
        String message="success";

        MessageBuilder.withDefault(message).send();

        // then
        var dictResult = KafkaTestUtil.getLastMessageByKey(dictConsumerFactory, "test-top", "1");
        assertTrue(dictResult.isPresent());
        assertEquals(message, dictResult.get().value());
    }
    
}
