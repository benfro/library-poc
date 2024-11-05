package net.benfro.library.userhub.api.person;

import net.benfro.library.userhub.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonConverterTest {

    Person person;
    PersonRequest request;

    @BeforeEach
    void setUp() {
        var uuid = UUID.randomUUID();

        person = Person.builder()
                .id(1L)
                .payload(Person.Payload.builder()
                        .firstName("John")
                        .lastName("the Baptist")
                        .email("john@saint.com")
                        .personId(uuid)
                        .build())
                .build();

        request = new PersonRequest(1L, "John", "the Baptist", "john@saint.com", uuid);
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
