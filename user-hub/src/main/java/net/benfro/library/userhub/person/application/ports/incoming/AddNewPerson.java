package net.benfro.library.userhub.person.application.ports.incoming;

import reactor.core.publisher.Mono;

public interface AddNewPerson {
    Mono<Long> handle(AddNewPersonCommand command);
}
