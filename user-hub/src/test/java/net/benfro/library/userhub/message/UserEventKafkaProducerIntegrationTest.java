package net.benfro.library.userhub.message;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.boot.test.context.SpringBootTest;

import net.benfro.library.userhub.AbstractKafkaIT;
import net.benfro.library.userhub.event.UserApplicationEvent;
import net.benfro.library.userhub.event.UserApplicationEventPublisher;
import net.benfro.library.userhub.person.adapter.outgoing.persistence.model.PersonEntity;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.benfro.library.userhub.person.application.model.UserEventMessageImpl;
import net.benfro.library.userhub.person.application.ports.outgoing.kafka.UserEventMessage;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.test.StepVerifier;

/**
 * Integration test
 */
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserEventKafkaProducerIntegrationTest extends AbstractKafkaIT {

    @Autowired
    UserApplicationEventPublisher publisher;

    @MockBean
    RSocketServerBootstrap rSocketServerBootstrap;

    @Test
    void test_user_message_is_put_on_kafka_topic_when_internal_event_is_published() {
        // Given
        UserApplicationEvent event = UserApplicationEvent.builder()
            .action(UserApplicationEvent.Action.USER_ADDED)
            .payload(PersonEntity.Payload.builder()
                .firstName("foo")
                .lastName("bar")
                .email("phlipp")
                .personId(UUID.randomUUID())
                .build())
            .build();

        Flux<ReceiverRecord<String, UserEventMessage>> flux =
            this.<UserEventMessage>createReceiver("user-events")
                .receive()
                .take(1);

        // When
        publisher.publishUserApplicationEvent(event);

        // Then
        StepVerifier.create(flux)
            .consumeNextWith(r -> assertEquals("foo", r.value().getFirstName()))
            .verifyComplete();
    }
}
