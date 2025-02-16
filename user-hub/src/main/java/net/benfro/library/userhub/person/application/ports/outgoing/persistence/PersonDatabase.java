package net.benfro.library.userhub.person.application.ports.outgoing.persistence;

import net.benfro.library.userhub.person.application.model.Person;
import reactor.core.publisher.Mono;

public interface PersonDatabase {

    Mono<Person> findById(Long id);

    Mono<Void> update(Person person);

    Mono<Long> save(Person person);
}
