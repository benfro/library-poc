package net.benfro.library.userhub.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import net.benfro.library.userhub.model.Person;
import net.benfro.library.userhub.test.IntegrationTest;

class PersonRepositoryTest implements IntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ReactiveTransactionManager transactionManager;

    private TransactionalOperator tx;

    @BeforeEach
    void setup() {
        tx = TransactionalOperator.create(transactionManager);
    }

    @Test
    void a_person_is_created_correctly() {

    }

    @Test
    void getById_should_return_empty_if_book_does_not_exist() {
        // given
        // no user

        // when
        var result = personRepository.getById(1234L).blockOptional();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void findById_should_return_persisted_person() {
        // given
        Long id = personRepository.reserveId().block();
        var person = Person.builder()
            .id(id)
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();
//
        // when
        personRepository.persist(person)
            .as(tx::transactional)
            .block();

        // then
        var persistedBook = personRepository.getById(id).block();

        assertThat(persistedBook).isEqualTo(person);
    }

    @Test
    void findAll_should_return_persisted_persons() {
        // given
        var book = Person.builder()
            .id(personRepository.reserveId().block())
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();
//
        // when
        personRepository.persist(book)
            .as(tx::transactional)
            .block();

        // then
        var users = personRepository.all().collectList().block();
        assertThat(users).hasSize(1);
    }

    @Test
    void findByISBN_should_return_matching_person() {
        // given
        var user = Person.builder()
            .id(1L)
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();
//
        // when
        personRepository.persist(user)
            .as(tx::transactional)
            .block();
        // when
        var retrievedUser = personRepository.findByEmail("per@pandersson.com").block();

        // then
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    void update_should_return_an_updated_person() {
        // given
        Long id = personRepository.reserveId().block();
        var user = Person.builder()
            .id(id)
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        personRepository.persist(user)
            .as(tx::transactional)
            .block();

        Person person = personRepository.getById(id).as(tx::transactional).block();

        // when
        person.setPayload(person.getPayload().withFirstName("Jane"));
        personRepository.update(person)
            .as(tx::transactional)
            .block();
        var retrievedUser = personRepository.findByEmail("per@pandersson.com").block();

        // then
        assertThat(retrievedUser.getPayload().getFirstName()).isEqualTo("Jane");
    }
}
