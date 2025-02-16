package net.benfro.library.userhub.api.person;

import net.benfro.library.userhub.person.adapter.outgoing.persistence.model.PersonEntity;

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
    PersonEntity personDtoToPerson(PersonDTO personDTO);

    @Mapping(target = "firstName", source = "payload.firstName")
    @Mapping(target = "lastName", source = "payload.lastName")
    @Mapping(target = "email", source = "payload.email")
    @Mapping(target = "personId", source = "payload.personId")
//    @Mapping(target = "withId", ignore = true)
    PersonDTO personToPersonDto(PersonEntity person);

    @Mapping(target = "payload", qualifiedByName = "toPayload")
    default PersonEntity updatePersonInstance(PersonDTO personDTO, @MappingTarget PersonEntity person) {
        if (Objects.isNull(person)) {
            return null;
        }
        person.setPayload(new PersonEntity.Payload(personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getEmail(),
                personDTO.getPersonId()));

        return person;
    }

    @Named("toPayload")
    default PersonEntity.Payload mapReqToPayload(PersonDTO personDTO) {
        return new PersonEntity.Payload(
                personDTO.getFirstName(),
                personDTO.getLastName(),
                personDTO.getEmail(),
                personDTO.getPersonId());
    }

}
