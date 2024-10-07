package net.benfro.library.userhub.model;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {

    Mono<Person> findByEmail(String email);
}
