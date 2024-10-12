package net.benfro.library.userhub.api.person;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import net.benfro.library.userhub.model.Person;

@Mapper
public interface PersonConverter {

    PersonConverter INSTANCE = Mappers.getMapper(PersonConverter.class);

    Person personRequestToPerson(PersonRequest personRequest);

    PersonRequest personToPersonRequest(Person person);

    PersonResponse personToPersonResponse(Person person);

    void updatePersonInstance(PersonRequest personRequest, @MappingTarget Person person);

}
