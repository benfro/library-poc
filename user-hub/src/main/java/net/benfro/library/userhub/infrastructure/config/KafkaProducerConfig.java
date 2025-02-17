package net.benfro.library.userhub.infrastructure.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import net.benfro.library.userhub.person.adapter.outgoing.kafka.UserEventKafkaProducer;
import net.benfro.library.userhub.person.application.ports.outgoing.kafka.UserEventMessage;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public SenderOptions<String, UserEventMessage> senderOptions(KafkaProperties properties) {
        return SenderOptions.create(properties.buildProducerProperties());
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, UserEventMessage> producerTemplate(SenderOptions<String, UserEventMessage> options) {
        return new ReactiveKafkaProducerTemplate<>(options);
    }

    @Bean
    public UserEventKafkaProducer userEventKafkaProducer(ReactiveKafkaProducerTemplate<String, UserEventMessage> producerTemplate) {
        var sink = Sinks.many().unicast().<UserEventMessage>onBackpressureBuffer();
        var flux = sink.asFlux();
        var eventProducer = new UserEventKafkaProducer(producerTemplate, sink, flux, "user-events");
        eventProducer.subscribe();
        return eventProducer;
    }
}
