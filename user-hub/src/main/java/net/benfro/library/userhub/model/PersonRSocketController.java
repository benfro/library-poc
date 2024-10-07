package net.benfro.library.userhub.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
//@RequiredArgsConstructor
public class PersonRSocketController {

    @Autowired
    private PersonRepository personRepository;

    @MessageMapping("findPersonById")
    public Mono<PersonResponse> findById(PersonRequest request) {
        return personRepository.findById(request.getId())
            .switchIfEmpty(Mono.error(new RuntimeException("Person not found")))
            .map(p -> PersonConverter.INSTANCE.personToPersonResponse(p))
            .doOnError(e -> log.error(e.getMessage(), e));
    }


}
