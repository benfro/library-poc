package net.benfro.library.userhub.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.api.person.PersonConverter;
import net.benfro.library.userhub.api.person.PersonRequest;
import net.benfro.library.userhub.api.person.PersonResponse;
import net.benfro.library.userhub.repository.PersonRepository;
import net.benfro.library.userhub.test.IntegrationTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
class PersonRSocketControllerTest implements IntegrationTest {

    private static RSocketRequester requester;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder,
                                 @Value("${spring.rsocket.server.port}") Integer port) {
        requester = builder
            .connectTcp("localhost", port)
            .block();
    }

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private ReactiveTransactionManager transactionManager;

    private TransactionalOperator tx;

    @BeforeEach
    void setup() {
        tx = TransactionalOperator.create(transactionManager);
    }

    //    @Transactional
    @Test
    void testRequestGetsResponse() {

        Long id = personRepository.reserveId().block();
        var p = Person.builder()
            .id(id)
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        personRepository.persist(p)
            .as(tx::transactional)
            .block();

        // Send a request message (1)
        PersonRequest data = new PersonRequest().withId(id);
        Mono<PersonResponse> result = requester
            .route("findPersonById")
            .data(data)
            .retrieveMono(PersonResponse.class);

        // Verify that the response message contains the expected data (2)
        StepVerifier
            .create(result)
            .consumeNextWith(message -> {
                assertEquals("Per", message.getFirstName());
            })
            .verifyComplete();
    }

    @Test
//    @Transactional
    void testUpdate() {

        Long id = personRepository.reserveId().block();
        var p = Person.builder()
            .id(id)
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        personRepository.persist(p)
            .as(tx::transactional)
            .block();

        Person person = personRepository.getById(id)
            .as(tx::transactional)
            .block();

        PersonRequest data = PersonConverter.INSTANCE.personToPersonRequest(person);
        data.setFirstName("PELLE");
        // Send a request message (1)
        Mono<Void> result = requester
            .route("updatePerson")
            .data(data)
            .retrieveMono(Void.class);

        // Verify that the response message contains the expected data (2)
        StepVerifier
            .create(result)
            .verifyComplete();

        Person savedBlock = personRepository.getById(id)
            .block();
        assertEquals("PELLE", savedBlock.getPayload().getFirstName());
    }

}
