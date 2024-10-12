package net.benfro.library.userhub.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import net.benfro.library.userhub.api.person.PersonConverter;
import net.benfro.library.userhub.api.person.PersonRequest;
import net.benfro.library.userhub.api.person.PersonResponse;
import net.benfro.library.userhub.repository.PersonRepository;
import net.benfro.library.userhub.test.IntegrationTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Person saved = personRepository.save(new Person(null, "per", "andersson", "per@pandersson.com"))
            .as(tx::transactional)
            .block();

        // Send a request message (1)
        PersonRequest data = new PersonRequest().withId(saved.getId());
        Mono<PersonResponse> result = requester
                .route("findPersonById")
                .data(data)
                .retrieveMono(PersonResponse.class);

        // Verify that the response message contains the expected data (2)
        StepVerifier
                .create(result)
                .consumeNextWith(message -> {
                    assertEquals("per", message.getFirstName());
                })
                .verifyComplete();
    }

    @Test
//    @Transactional
    void testUpdate() {

        Person saved = personRepository.save(new Person(null, "per", "andersson", "per@pandersson.com"))
            .as(tx::transactional)
            .block();

        PersonRequest data = PersonConverter.INSTANCE.personToPersonRequest(saved);
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

        Person savedBlock = personRepository.findById(saved.getId())
            .block();
        assertEquals("PELLE", savedBlock.getFirstName());
    }

}
