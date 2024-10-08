package net.benfro.library.userhub.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Testcontainers
@SpringBootTest
@EnableR2dbcAuditing
class PersonRSocketControllerTest {

    private static RSocketRequester requester;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withPassword("postgres").withUsername("postgres").withDatabaseName("person_service");

    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder,
                                 @Value("${spring.rsocket.server.port}") Integer port) {
        postgresContainer.start();

        requester = builder
                .connectTcp("localhost", port)
                .block();
    }

    @Autowired
    PersonRepository personRepository;

    //    @Transactional
    @Test
    void testRequestGetsResponse() {

        personRepository.save(new Person(null, "per", "andersson", "per@pandersson.com")).block();

        personRepository.findById(1L).subscribe(i -> log.info("Found {}", i));

        // Send a request message (1)
        PersonRequest data = new PersonRequest().withId(1L);
        Mono<PersonResponse> result = requester
                .route("findPersonById")
                .data(data)
                .retrieveMono(PersonResponse.class);

        // Verify that the response message contains the expected data (2)
        StepVerifier
                .create(result)
                .consumeNextWith(message -> {
                    assertEquals("per", message.getFirstName());
//                assertThat(message.getIndex()).isEqualTo(0);
                })
                .verifyComplete();
    }

}
