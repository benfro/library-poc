package net.benfro.library.userhub.api.person;

import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import net.benfro.library.userhub.model.Person;

@Mapper
public interface PersonConverter {

    PersonConverter INSTANCE = Mappers.getMapper(PersonConverter.class);

    @Mapping(source = "firstName", target = "payload.firstName")
    @Mapping(source = "lastName", target = "payload.lastName")
    @Mapping(source = "email", target = "payload.email")
    Person personRequestToPerson(PersonRequest personRequest);

    @Mapping(target = "firstName", source = "payload.firstName")
    @Mapping(target = "lastName", source = "payload.lastName")
    @Mapping(target = "email", source = "payload.email")
//    @Mapping(target = "withId", ignore = true)
    PersonRequest personToPersonRequest(Person person);

    @Mapping(target = "firstName", source = "payload.firstName")
    @Mapping(target = "lastName", source = "payload.lastName")
    @Mapping(target = "email", source = "payload.email")
    PersonResponse personToPersonResponse(Person person);

//    @Mapping(source = "firstName", target = "payload.firstName")
//    @Mapping(source = "lastName", target = "payload.lastName")
    @Mapping(target = "payload", qualifiedByName = "toPayload")
    default Person updatePersonInstance(PersonRequest personRequest, @MappingTarget Person person) {
        if (Objects.isNull(person)) {
            return null;
        }
        person.setPayload(new Person.Payload(personRequest.getFirstName(),
            personRequest.getLastName(),
            personRequest.getEmail()));

        return person;
    }

    @Named("toPayload")
    default Person.Payload mapReqToPayload(PersonRequest personRequest) {
        return new Person.Payload(personRequest.getFirstName(), personRequest.getLastName(), personRequest.getEmail());
    }

}

//@Mapping(target = "fish.kind", source = "source.fish.type")
//@Mapping(target = "material.materialType", source = "source.material")
//@Mapping(target = "quality.document", source = "source.quality.report")
//@Mapping(target = "volume", source = "source")
//abstract FishTankWithVolumeDto map(FishTank source);
//
//VolumeDto mapVolume(FishTank source) {
//    int volume = source.length * source.width * source.height;
//    String desc = volume < 100 ? "Small" : "Large";
//    return new VolumeDto(volume, desc);
//}
