package net.benfro.library.userhub.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import net.benfro.library.userhub.model.Person;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends R2dbcRepository<Person, Long> {

    Mono<Person> findByEmail(String email);
}
