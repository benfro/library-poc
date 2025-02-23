package net.benfro.library.userhub.person.application.ports.incoming;

import reactor.core.publisher.Mono;

public interface DeletePerson {

    Mono<Void> handle(DeletePersonCommand command);
}
