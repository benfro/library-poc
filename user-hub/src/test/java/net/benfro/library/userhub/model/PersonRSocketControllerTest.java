package net.benfro.library.userhub.model;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PersonRSocketControllerTest {

    private static RSocketRequester requester;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder,
                                 @Value("${spring.rsocket.server.port}") Integer port) {
        requester = builder
            .connectTcp("localhost", port)
            .block();
    }

    @Test
    void testRequestGetsResponse() {
        // Send a request message (1)
        PersonRequest data = new PersonRequest().withId(1L);
        Mono<PersonResponse> result = requester
            .route("request-response")
            .data(data)
            .retrieveMono(PersonResponse.class);

        // Verify that the response message contains the expected data (2)
        StepVerifier
            .create(result)
            .consumeNextWith(message -> {
//                assertThat(message.getOrigin()).isEqualTo(RSocketController.SERVER); assertThat(message.getInteraction()).isEqualTo(RSocketController.RESPONSE);
//                assertThat(message.getIndex()).isEqualTo(0);
            })
            .verifyComplete();
    }

}
