package net.benfro.library.userhub.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
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

    @AfterAll
    public static void tearDownOnce() {
        requester.rsocket().dispose();
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

    @Test
    void findPersonById_path_should_return_a_person() {

        var p = Person.builder()
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        var generatedId = personRepository.persist(p)
            .as(tx::transactional)
            .block();

        // Send a request message (1)
        PersonRequest data = new PersonRequest().withId(generatedId);
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
    void updatePerson_path_should_update_the_person() {
        // Given
        var p = Person.builder()
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        var generatedId = personRepository.persist(p)
            .as(tx::transactional)
            .block();

        Person person = personRepository.getById(generatedId)
            .as(tx::transactional)
            .block();

        PersonRequest data = PersonConverter.INSTANCE.personToPersonRequest(person);
        data.setFirstName("PELLE");
        // When: Send a request message (1)
        Mono<Void> result = requester
            .route("updatePerson")
            .data(data)
            .retrieveMono(Void.class);

        // Then: Verify that the response message contains the expected data (2)
        StepVerifier
            .create(result)
            .verifyComplete();

        Person savedBlock = personRepository.getById(generatedId)
            .block();
        assertEquals("PELLE", savedBlock.getPayload().getFirstName());
    }

    @Test
    void createPerson_path_should_create_a_person() {
        // Given
        var personRequest = PersonRequest.builder()
            .firstName("Per")
            .lastName("andersson")
            .email("per@pandersson.com")
            .build();

        // When: Send a request message (1)
        Mono<Long> result = requester
            .route("createPerson")
            .data(personRequest)
            .retrieveMono(Long.class);

        // Then: Verify that the response message contains the expected data (2)
        StepVerifier.create(result)
            .expectNextMatches(id -> id > 0)
            .verifyComplete();

        var person = personRepository.getById(result.block()).as(tx::transactional).block();
        assertEquals("Per", person.getPayload().getFirstName());
        assertNotNull(person.getPayload().getPersonId());
    }
}
