package com.thoughtworks.enterprise.travel.builder;

import com.thoughtworks.enterprise.travel.util.SpringContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class MessageBuilder {
    private static final String TOPIC = "test-top";

    private final String message;

    public static MessageBuilder withDefault(String message) {
        return new MessageBuilder(message);
    }

    @SneakyThrows
    public void send() {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        //noinspection unchecked
        KafkaTemplate<String, String> template = applicationContext.getBean(KafkaTemplate.class);
        template.send( TOPIC, "1",  message );
        TimeUnit.SECONDS.sleep(1);
    }

}
