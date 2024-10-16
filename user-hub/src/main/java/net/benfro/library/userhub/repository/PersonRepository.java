package net.benfro.library.userhub.repository;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.r2dbc.spi.Readable;
import net.benfro.library.userhub.model.Person;
import net.benfro.library.userhub.repository.sql.PersonQueries;
import net.benfro.library.userhub.repository.sql.Squtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepository {

    private final Squtils sql = new Squtils("person", "id", "first_name, last_name, email");
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
    public Mono<Void> persist(Person book) {
        Validate.notNull(book, "book can't be null");

        final Long id = book.getId();
        return db.sql(sql.insert())
            .bind("id", id)
            .bind("first_name", book.getPayload().firstName())
            .bind("last_name", book.getPayload().lastName())
            .bind("email", book.getPayload().email())
            .then();
//        return getById(id);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> update(Person book) {
        Validate.notNull(book, "Book can't be null");

        return db.sql(sql.update())
            .bind("id", book.getId())
            .bind("first_name", book.getPayload().firstName())
            .bind("last_name", book.getPayload().lastName())
            .bind("email", book.getPayload().email())
            .then();
    }

    /**
     * Get all books.
     *
     * @return A {@link Flux} of all books.
     */
    public Flux<Person> all() {
        return db.sql(sql.selectAll())
            .map(rowToPerson())
            .all();
    }

    public Mono<Person> findByEmail(String email) {
        Validate.notEmpty(email, "Email can't be null or empty");
        return db.sql(sql.selectAllWhere("email"))
//        return db.sql(BookQueries.SELECT_BY_ISBN)
            .bind("email", email)
            .map(rowToPerson())
            .one();
    }

    public Mono<Person> getById(Long id) {
        Validate.notNull(id, "id can't be null");

        return db.sql(sql.selectById())
            .bind("id", id)
            .map(rowToPerson())
            .one();
    }

    public Flux<Person> getByIds(List<Long> ids) {
        Validate.notNull(ids, "ids can't be null");

        if (ids.isEmpty()) {
            return Flux.empty();
        }

        return db.sql(sql.selectByIds())
            .bind("ids", ids)
            .map(rowToPerson())
            .all();
    }

    private Function<Readable, Person> rowToPerson() {
        return r -> Person.builder()
            .id(r.get("id", Long.class))
            .payload(Person.Payload.builder()
                .firstName(r.get("first_name", String.class))
                .lastName(r.get("last_name", String.class))
                .email(r.get("email", String.class))
                .build())
            .build();
    }
}
