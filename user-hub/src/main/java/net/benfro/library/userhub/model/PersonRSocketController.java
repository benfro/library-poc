package net.benfro.library.userhub.model;

import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import net.benfro.library.userhub.api.person.PersonConverter;
import net.benfro.library.userhub.api.person.PersonRequest;
import net.benfro.library.userhub.api.person.PersonResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
//@RequiredArgsConstructor
public class PersonRSocketController {

    @Autowired
    private PersonRepository personRepository;

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
            .doOnNext(person -> PersonConverter.INSTANCE.updatePersonInstance(request, person))
            .flatMap(person -> personRepository.update(person))
            .switchIfEmpty(Mono.error(new RuntimeException("Person not found")))
            .then();
    }

//    @Transactional
//    Mono<Book> save(Book book){
//        return repository.findByIsbn(book.getIsbn())
//            .flatMap(found -> repository.save(found.copyMutableValuesFrom(book)))
//            .switchIfEmpty(repository.save(book));
//    }


}
