package net.benfro.library.userhub.message;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@RequiredArgsConstructor
public class UserEventKafkaProducer {

    private final ReactiveKafkaProducerTemplate<String, UserEventMessage> template;
    private final Sinks.Many<UserEventMessage> sink;
    private final Flux<UserEventMessage> flux;
    private final String topic;

    public void subscribe() {
        var srFlux = this.flux
            .map(e -> new ProducerRecord<>(topic, e.getPersonId().toString(), e))
            .map(pr -> SenderRecord.create(pr, pr.key()));

        this.template.send(srFlux)
            .doOnNext(r -> log.info("emmitted event {}", r.correlationMetadata()))
            .subscribe();
    }

    public void emitEvent(UserEventMessage msg) {
        this.sink.tryEmitNext(msg);
    }
}
