package net.benfro.library.userhub.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

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
    void persist_should_generate_id_and_add_a_uuid_if_not_provided() {
        // given
        var user = Person.builder()
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        // when
        var generatedId = personRepository.persist(user)
            .as(tx::transactional)
            .block();

        // then
        assertThat(generatedId).isNotNull().isPositive();

        // and
        Person person = personRepository.getById(generatedId).as(tx::transactional).block();
        assertThat(person.getPayload().getPersonId()).isNotNull();
    }

    @Test
    void getById_should_return_empty_if_person_does_not_exist() {
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
        var person = Person.builder()
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .personId(UUID.randomUUID())
                .build())
            .build();

        // when
        var generatedId = personRepository.persist(person)
            .as(tx::transactional)
            .block();
        person.setId(generatedId);

        // then
        var persistedPerson = personRepository.getById(generatedId).block();

        assertThat(persistedPerson).isEqualTo(person);
    }

    @Test
    void findAll_should_return_persisted_persons() {
        // given
        var person = Person.builder()
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .build())
            .build();

        var person2 = person
            .withPayload(
                person.getPayload().withEmail("a@b.com")
            );

        // when
        personRepository.persist(person)
            .as(tx::transactional)
            .block();
        personRepository.persist(person2)
            .as(tx::transactional)
            .block();

        // then
        var users = personRepository.all().collectList().block();
        assertThat(users).hasSize(2);
    }

    @Test
    void findByISBN_should_return_matching_person() {
        // given
        var user = Person.builder()
            .payload(Person.Payload.builder()
                .firstName("Per")
                .lastName("andersson")
                .email("per@pandersson.com")
                .personId(UUID.randomUUID())
                .build())
            .build();
//
        var generatedId = personRepository.persist(user)
            .as(tx::transactional)
            .block();
        user.setId(generatedId);
        // when
        var retrievedUser = personRepository.findByEmail("per@pandersson.com").block();

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

        var generatedId = personRepository.persist(user)
            .as(tx::transactional)
            .block();

        Person person = personRepository.getById(generatedId).as(tx::transactional).block();

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
