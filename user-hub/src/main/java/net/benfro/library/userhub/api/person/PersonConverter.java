package net.benfro.library.userhub.api.person;

import net.benfro.library.userhub.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface PersonConverter {

    PersonConverter INSTANCE = Mappers.getMapper(PersonConverter.class);

    @Mapping(source = "firstName", target = "payload.firstName")
    @Mapping(source = "lastName", target = "payload.lastName")
    @Mapping(source = "email", target = "payload.email")
    @Mapping(source = "personId", target = "payload.personId")
    Person personDtoToPerson(PersonDTO personDTO);

    @Mapping(target = "firstName", source = "payload.firstName")
    @Mapping(target = "lastName", source = "payload.lastName")
    @Mapping(target = "email", source = "payload.email")
    @Mapping(target = "personId", source = "payload.personId")
//    @Mapping(target = "withId", ignore = true)
    PersonDTO personToPersonDto(Person person);

    @Mapping(target = "payload", qualifiedByName = "toPayload")
    default Person updatePersonInstance(PersonDTO personDTO, @MappingTarget Person person) {
        if (Objects.isNull(person)) {
            return null;
        }
        person.setPayload(new Person.Payload(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getEmail(),
                personDTO.getPersonId()));

        return person;
    }

    @Named("toPayload")
    default Person.Payload mapReqToPayload(PersonDTO personDTO) {
        return new Person.Payload(
                personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getEmail(),
                personDTO.getPersonId());
    }

}
