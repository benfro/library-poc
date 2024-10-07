package net.benfro.library.userhub.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonConverter {

    PersonConverter INSTANCE = Mappers.getMapper(PersonConverter.class);

    Person personRequestToPerson(PersonRequest personRequest);

    PersonResponse personToPersonResponse(Person person);

}
