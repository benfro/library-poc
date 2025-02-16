package net.benfro.library.userhub.person.adapter.outgoing.persistence;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.benfro.library.userhub.person.adapter.outgoing.persistence.model.PersonEntity;
import net.benfro.library.userhub.person.application.model.Person;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "firstName", target = "payload.firstName")
    @Mapping(source = "lastName", target = "payload.lastName")
    @Mapping(source = "email", target = "payload.email")
    @Mapping(source = "personId", target = "payload.personId")
    PersonEntity personDtoToPerson(Person personDTO);

    @Mapping(target = "firstName", source = "payload.firstName")
    @Mapping(target = "lastName", source = "payload.lastName")
    @Mapping(target = "email", source = "payload.email")
    @Mapping(target = "personId", source = "payload.personId")
//    @Mapping(target = "withId", ignore = true)
    Person personToPersonDto(PersonEntity person);
}
