package net.benfro.poc.library.domain;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Person extends PanacheEntity {

//    @Id
//    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String loanCardNumber;

    public static Uni<Person> findByEmail(String email){
        return find("email", email).firstResult();
    }

}
