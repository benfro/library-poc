package net.benfro.library.userhub.event;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.benfro.library.userhub.message.UserEventKafkaProducer;
import net.benfro.library.userhub.message.UserEventMapper;
import net.benfro.library.userhub.model.Person;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserApplicationEventListenerTest {

    @Autowired
    UserApplicationEventPublisher publisher;

    @MockBean
    UserEventKafkaProducer producer;

    @MockBean
    RSocketServerBootstrap rSocketServerBootstrap;

    @Test
    void test_application_event_is_received_and_kafka_producer_sends_message() {
        // Given
        UserApplicationEvent event = UserApplicationEvent.builder()
            .action(UserApplicationEvent.Action.USER_ADDED)
            .payload(Person.Payload.builder()
                .firstName("foo")
                .lastName("bar")
                .email("phlipp")
                .personId(UUID.randomUUID())
                .build())
            .build();

        // When
        publisher.publishUserApplicationEvent(event);

        // Then
        Mockito
            .verify(producer)
            .emitEvent(UserEventMapper.INSTANCE.fromUserToUserEventMessage(event));
    }
}
