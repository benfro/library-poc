package net.benfro.library.userhub.person.adapter.outgoing.persistence;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import net.benfro.library.userhub.person.application.model.Person;
import net.benfro.library.userhub.person.application.ports.outgoing.persistence.PersonDatabase;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class PersonDatabaseAdapter implements PersonDatabase {

    private final PersonRepository repository;

    @Override
    public Mono<Person> findById(Long id) {
        return repository.getById(id).map(PersonMapper.INSTANCE::personToPersonDto);
    }

    @Override
    public Mono<Void> update(Person person) {
        return repository.update(PersonMapper.INSTANCE.personDtoToPerson(person));
    }

    @Override
    public Mono<Long> save(Person person) {
        return repository.persist(PersonMapper.INSTANCE.personDtoToPerson(person));
    }
}
