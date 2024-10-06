package net.benfro.poc.library.api.person;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/person")
public class PersonController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Person> findPerson(@QueryParam(value = "id")long id) {
        log.info("QQQ");
        return Uni.createFrom().emitter(emitter -> new Person());
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Uni<Person> findPersonFromEmail(@QueryParam(value = "email") String email) {
//        return Uni.createFrom().nullItem();
//    }
}
