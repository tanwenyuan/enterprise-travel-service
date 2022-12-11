package com.thoughtworks.travel.infrastructure.message;

import com.thoughtworks.travel.basetest.ApplicationTestBase;
import com.thoughtworks.travel.builder.MessageBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import static com.thoughtworks.travel.util.KafkaTestUtil.getLastMessageByKey;
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
        var dictResult = getLastMessageByKey(dictConsumerFactory, "test-top", "1");
        assertTrue(dictResult.isPresent());
        assertEquals(message, dictResult.get().value());
    }
    
}
