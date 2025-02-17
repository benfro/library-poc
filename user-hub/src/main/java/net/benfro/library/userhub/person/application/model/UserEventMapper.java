package net.benfro.library.userhub.person.application.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import net.benfro.library.userhub.event.UserApplicationEvent;
import net.benfro.library.userhub.person.application.ports.outgoing.kafka.UserEventMessage;

@Mapper
public interface UserEventMapper {

    UserEventMapper INSTANCE = Mappers.getMapper(UserEventMapper.class);

    @Mapping(source = "payload.firstName", target = "firstName")
    @Mapping(source = "payload.lastName", target = "lastName")
    @Mapping(source = "payload.email", target = "email")
    @Mapping(source = "payload.personId", target = "personId")
    UserEventMessage fromUserToUserEventMessage(UserApplicationEvent appEvent);
}
