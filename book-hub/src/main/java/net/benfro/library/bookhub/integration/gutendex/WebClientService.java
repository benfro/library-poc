package net.benfro.library.bookhub.integration.gutendex;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService(@Autowired WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .defaultHeader("Content-Type", "application/json" )
            .baseUrl("https://gutendex.com")
            .build();
    }

    public Flux<String> findUsers() {
        return Flux.generate(
            () -> "/books",
            (value, sink) -> {
                Mono<String> bookListMono = getBookList(value)
                    .doOnNext(e -> sink.next(e))
                    .doOnError(ex -> log.error("Error retrieving book list", ex));
                return "/books";
            }
        );
    }

    private Mono<String> getBookList(String uri) {
        return this.webClient.get().uri(uri)
            .retrieve()
            .bodyToMono(String.class);
    }

}
