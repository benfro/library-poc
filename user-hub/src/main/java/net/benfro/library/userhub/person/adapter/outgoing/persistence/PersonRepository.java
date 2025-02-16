package net.benfro.library.userhub.person.adapter.outgoing.persistence;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.r2dbc.spi.Readable;
import net.benfro.library.commons.Squtils;
import net.benfro.library.userhub.person.adapter.outgoing.persistence.model.PersonEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepository {

    private final Squtils sql = new Squtils("person", "id", "first_name, last_name, email, person_id");
    private final DatabaseClient db;

    @Autowired
    public PersonRepository(DatabaseClient db) {
        this.db = db;
    }

    public Mono<Long> reserveId() {
        return db.sql(PersonQueries.RESERVE_ID)
            .mapValue(Long.class)
            .one();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Long> persist(PersonEntity person) {
        Validate.notNull(person, "Person can't be null");

        final Long id = person.getId();

        return db.sql(sql.insertNoId())
//            .bind("id", id)
            .bind("first_name", person.getPayload().firstName())
            .bind("last_name", person.getPayload().lastName())
            .bind("email", person.getPayload().email())
            .bind("person_id",
                Objects.isNull(person.getPayload().personId()) ? UUID.randomUUID() : person.getPayload().personId())
            .filter(s -> s.returnGeneratedValues("id"))
            .map(row -> row.get("id", Long.class))
            .first();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> update(PersonEntity person) {
        Validate.notNull(person, "Person can't be null");
        // TODO Update should not include non-updatable fields
        return db.sql(sql.update())
            .bind("id", person.getId())
            .bind("first_name", person.getPayload().firstName())
            .bind("last_name", person.getPayload().lastName())
            .bind("email", person.getPayload().email())
            .bind("person_id", person.getPayload().personId())
            .then();
    }

    /**
     * Get all books.
     *
     * @return A {@link Flux} of all books.
     */
    public Flux<PersonEntity> all() {
        return db.sql(sql.selectAll())
            .map(rowToPerson())
            .all();
    }

    public Mono<PersonEntity> findByEmail(String email) {
        Validate.notEmpty(email, "Email can't be null or empty");
        return db.sql(sql.selectAllWhere("email"))
            .bind("email", email)
            .map(rowToPerson())
            .one();
    }

    public Mono<PersonEntity> findByPersonId(String personId) {
        Validate.notEmpty(personId, "Person ID can't be null or empty");
        return db.sql(sql.selectAllWhere("person_id"))
            .bind("person_id", personId)
            .map(rowToPerson())
            .one();
    }

    public Mono<PersonEntity> getById(Long id) {
        Validate.notNull(id, "id can't be null");

        return db.sql(sql.selectById())
            .bind("id", id)
            .map(rowToPerson())
            .one();
    }

    public Flux<PersonEntity> getByIds(List<Long> ids) {
        Validate.notNull(ids, "ids can't be null");

        if (ids.isEmpty()) {
            return Flux.empty();
        }

        return db.sql(sql.selectByIds())
            .bind("ids", ids)
            .map(rowToPerson())
            .all();
    }

    private Function<Readable, PersonEntity> rowToPerson() {
        return r -> PersonEntity.builder()
            .id(r.get("id", Long.class))
            .payload(PersonEntity.Payload.builder()
                .firstName(r.get("first_name", String.class))
                .lastName(r.get("last_name", String.class))
                .email(r.get("email", String.class))
                .personId(r.get("person_id", UUID.class))
                .build())
            .build();
    }
}
