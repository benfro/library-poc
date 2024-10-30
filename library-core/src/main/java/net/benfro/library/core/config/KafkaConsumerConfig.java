package net.benfro.library.core.config;

import java.util.List;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;
import net.benfro.library.core.message.UserEvent;
import reactor.kafka.receiver.ReceiverOptions;

@SuppressWarnings("removal")
@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ReceiverOptions<String, UserEvent> userEventReceiverOptions(KafkaProperties kafkaProperties) {
        return ReceiverOptions.<String, UserEvent>create(kafkaProperties.buildConsumerProperties())
            // Keep header information
            // These properties can be set in *.yaml file as well
            .consumerProperty(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, false)
            .consumerProperty(JsonDeserializer.USE_TYPE_INFO_HEADERS, false)
            .consumerProperty(JsonDeserializer.VALUE_DEFAULT_TYPE, UserEvent.class)
            .subscription(List.of("user-events"));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, UserEvent> userEventKafkaConsumerTemplate(ReceiverOptions<String, UserEvent> options) {
        return new ReactiveKafkaConsumerTemplate<>(options);
    }
}
