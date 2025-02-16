package net.benfro.library.userhub.api.person;

import net.benfro.library.userhub.person.adapter.outgoing.persistence.model.PersonEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonEntityConverterTest {

    PersonEntity person;
    PersonDTO request;

    @BeforeEach
    void setUp() {
        var uuid = UUID.randomUUID();

        person = PersonEntity.builder()
                .id(1L)
                .payload(PersonEntity.Payload.builder()
                        .firstName("John")
                        .lastName("the Baptist")
                        .email("john@saint.com")
                        .personId(uuid)
                        .build())
                .build();

        request = new PersonDTO(1L, "John", "the Baptist", "john@saint.com", uuid);
    }

    @Test
    void personDtoToPerson() {
        PersonEntity result = PersonConverter.INSTANCE.personDtoToPerson(request);

        assertAll("All",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("John", result.getPayload().getFirstName()),
                () -> assertEquals("the Baptist", result.getPayload().getLastName()),
                () -> assertEquals("john@saint.com", result.getPayload().getEmail())
        );

    }

    @Test
    void personToPersonDto() {
        PersonDTO result = PersonConverter.INSTANCE.personToPersonDto(person);

        assertAll("All",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("the Baptist", result.getLastName()),
                () -> assertEquals("john@saint.com", result.getEmail())
        );
    }

    @Test
    void personToPersonDTO() {
        PersonDTO result = PersonConverter.INSTANCE.personToPersonDto(person);

        assertAll("All",
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("the Baptist", result.getLastName()),
                () -> assertEquals("john@saint.com", result.getEmail())
        );
    }

    @Test
    void updatePersonInstance() {
        request.setFirstName("Holy Johnny");
        PersonConverter.INSTANCE.updatePersonInstance(request, person);

        assertAll("All",
                () -> assertEquals(1L, person.getId()),
                () -> assertEquals("Holy Johnny", person.getPayload().getFirstName()),
                () -> assertEquals("the Baptist", person.getPayload().getLastName()),
                () -> assertEquals("john@saint.com", person.getPayload().getEmail())
        );
    }
}
