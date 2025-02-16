package net.benfro.library.userhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.api.person.PersonConverter;
import net.benfro.library.userhub.api.person.PersonDTO;
import net.benfro.library.userhub.event.UserApplicationEvent;
import net.benfro.library.userhub.event.UserApplicationEventPublisher;
import net.benfro.library.userhub.person.adapter.outgoing.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserApplicationEventPublisher userEventPublisher;

    public Mono<PersonDTO> findById(PersonDTO request) {
        return personRepository.getById(request.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Person not found")))
                .map(p -> PersonConverter.INSTANCE.personToPersonDto(p))
                .doOnError(e -> log.error(e.getMessage(), e));
    }

    public Mono<Void> updatePerson(PersonDTO request) {
        return personRepository.getById(request.getId())
                .switchIfEmpty(Mono.error(new RuntimeException("Person not found")))
                .map(person -> PersonConverter.INSTANCE.updatePersonInstance(request, person))
                .flatMap(person -> personRepository.update(person))
                .then();
    }

    public Mono<Long> createPerson(PersonDTO request) {
        return Mono.just(request)
                .map(pr -> PersonConverter.INSTANCE.personDtoToPerson(pr))
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
