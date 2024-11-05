package net.benfro.library.userhub.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.api.person.PersonConverter;
import net.benfro.library.userhub.api.person.PersonRequest;
import net.benfro.library.userhub.api.person.PersonResponse;
import net.benfro.library.userhub.message.UserEventKafkaProducer;
import net.benfro.library.userhub.message.UserEventMapper;
import net.benfro.library.userhub.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
//@RequiredArgsConstructor
public class PersonRSocketController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserEventKafkaProducer userEventKafkaProducer;

    @MessageMapping("findPersonById")
    public Mono<PersonResponse> findById(PersonRequest request) {
        return personRepository.getById(request.getId())
            .switchIfEmpty(Mono.error(new RuntimeException("Person not found")))
            .map(p -> PersonConverter.INSTANCE.personToPersonResponse(p))
            .doOnError(e -> log.error(e.getMessage(), e));
    }

    @Transactional
    @MessageMapping("updatePerson")
    public Mono<Void> updatePerson(PersonRequest request) {
        return personRepository.getById(request.getId())
            .switchIfEmpty(Mono.error(new RuntimeException("Person not found")))
            .map(person -> PersonConverter.INSTANCE.updatePersonInstance(request, person))
            .flatMap(person -> personRepository.update(person))
            .then();
    }

    @Transactional
    @MessageMapping("createPerson")
    public Mono<Long> createPerson(PersonRequest request) {
        return Mono.just(request)
            .map(pr -> PersonConverter.INSTANCE.personRequestToPerson(pr))
            .flatMap(p -> personRepository.persist(p))
            .doOnNext(e -> {
                personRepository.getById(e)
                    .map(p -> UserEventMapper.INSTANCE.fromUserToUserEventMessage(p))
                    .doOnNext(uem -> this.userEventKafkaProducer.emitEvent(uem))
                    .then();
            })
            .doOnError(e -> log.error("there was an error: {}", e.getMessage()));
    }

}
