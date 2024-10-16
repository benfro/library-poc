package net.benfro.library.userhub.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        var book = Person.builder()
                .payload(Person.Payload.builder()
                        .firstName("Per")
                        .lastName("andersson")
                        .email("per@pandersson.com")
                        .build())
                .build();
//
        // when
        var saved = personRepository.persist(book)
            .as(tx::transactional)
            .block();

        // then
        var persistedBook = personRepository.getById(saved.getId()).block();

        assertThat(persistedBook).isEqualTo(book);
    }

    @Test
    void findAll_should_return_persisted_persons() {
        // given
        var book = Person.builder()
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
        var retrievedUser = personRepository.findByEmail("john.doe@example.com").block();

        // then
        assertThat(retrievedUser).isEqualTo(user);
    }

    @Test
    void update_should_return_an_updated_person() {
        // given
        var user = Person.builder()
                .payload(Person.Payload.builder()
                        .firstName("Per")
                        .lastName("andersson")
                        .email("per@pandersson.com")
                        .build())
                .build();

        Person saved = personRepository.persist(user)
            .as(tx::transactional)
            .block();

        // when
        saved.setPayload(saved.getPayload().withFirstName("Jane"));
        personRepository.update(saved)
                .as(tx::transactional)
                .block();
        var retrievedUser = personRepository.findByEmail("john.doe@example.com").block();

        // then
        assertThat(retrievedUser).isEqualTo(user);
    }
}
