package net.benfro.library.userhub.api.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.event.UserApplicationEvent;
import net.benfro.library.userhub.event.UserApplicationEventPublisher;
import net.benfro.library.userhub.message.UserEventKafkaProducer;
import net.benfro.library.userhub.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class PersonRSocketController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserApplicationEventPublisher userEventPublisher;

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
                    .doOnNext(uem -> this.userEventPublisher
                        .publishUserApplicationEvent(UserApplicationEvent.ofAdded(uem.getPayload())))
                    .then();
            })
            .doOnError(e -> log.error("there was an error: {}", e.getMessage()));
    }

}
