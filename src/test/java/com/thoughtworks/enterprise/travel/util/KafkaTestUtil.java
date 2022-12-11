package com.thoughtworks.enterprise.travel.util;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.assertj.core.util.Streams;
import org.springframework.kafka.core.ConsumerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

public class KafkaTestUtil {

    public static <T> ConsumerRecords<String, T> consumeFromTopic(ConsumerFactory<String, T> consumerFactory,
                                                                  String topic) {
        ConsumerRecords<String, T> records;
        try (Consumer<String, T> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(topic));
            records = consumer.poll(Duration.ofSeconds(3));
        }
        return records;
    }

    public static <T> Optional<ConsumerRecord<String, T>> getLastMessageByKey(
            ConsumerFactory<String, T> consumerFactory, String topic, String key) {
        ConsumerRecords<String, T> records = consumeFromTopic(consumerFactory, topic);
        return Streams.stream(records).filter(record -> record.key().equals(key)).max(
                Comparator.comparing(ConsumerRecord::offset));
    }

}
