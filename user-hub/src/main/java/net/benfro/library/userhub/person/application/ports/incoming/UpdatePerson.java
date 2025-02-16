package net.benfro.library.userhub.person.application.ports.incoming;

import reactor.core.publisher.Mono;

public interface UpdatePerson {

    Mono<Void> handle(UpdatePersonCommand command);

}
