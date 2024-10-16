package net.benfro.library.userhub.api.person;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.benfro.library.userhub.model.Person;

class PersonConverterTest {

    Person person;
    PersonRequest request;

    @BeforeEach
    void setUp() {
        person = Person.builder()
            .id(1L)
            .payload(Person.Payload.builder()
                .firstName("John")
                .lastName("the Baptist")
                .email("john@saint.com")
                .build())
            .build();

        request = new PersonRequest(1L, "John", "the Baptist", "john@saint.com");
    }

    @Test
    void personRequestToPerson() {
        Person result = PersonConverter.INSTANCE.personRequestToPerson(request);

        assertAll("All",
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals("John", result.getPayload().getFirstName()),
            () -> assertEquals("the Baptist", result.getPayload().getLastName()),
            () -> assertEquals("john@saint.com", result.getPayload().getEmail())
        );

    }

    @Test
    void personToPersonRequest() {
        PersonRequest result = PersonConverter.INSTANCE.personToPersonRequest(person);

        assertAll("All",
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals("John", result.getFirstName()),
            () -> assertEquals("the Baptist", result.getLastName()),
            () -> assertEquals("john@saint.com", result.getEmail())
        );
    }

    @Test
    void personToPersonResponse() {
        PersonResponse result = PersonConverter.INSTANCE.personToPersonResponse(person);

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
