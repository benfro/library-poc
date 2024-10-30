package net.benfro.library.core.message;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.core.event.UserCreatedApplicationEvent;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventSubscriber {

    private final ReactiveKafkaConsumerTemplate<String, UserEvent> template;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void subscribe() {

        template.receiveAutoAck()
            .flatMap(record -> {
                return Mono.just(record);
            })
            .doOnNext(record -> publishEvent(record.value()))
            .subscribe();
    }

    void publishEvent(UserEvent event) {
        var userCreatedApplicationEvent = new UserCreatedApplicationEvent(this, event);
        applicationEventPublisher.publishEvent(userCreatedApplicationEvent);
    }
}
