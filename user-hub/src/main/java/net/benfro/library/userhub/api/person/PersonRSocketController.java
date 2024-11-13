package net.benfro.library.userhub.api.person;

import lombok.RequiredArgsConstructor;
import net.benfro.library.userhub.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.event.UserApplicationEvent;
import net.benfro.library.userhub.event.UserApplicationEventPublisher;
import net.benfro.library.userhub.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PersonRSocketController {

    private final PersonService personService;

    @MessageMapping({"findPersonById"})
    public Mono<PersonDTO> findById(PersonDTO request) {
        return personService.findById(request);
    }

    @Transactional
    @MessageMapping("updatePerson")
    public Mono<Void> updatePerson(PersonDTO request) {
        return personService.updatePerson(request);
    }

    @Transactional
    @MessageMapping("createPerson")
    public Mono<Long> createPerson(PersonDTO request) {
        return personService.createPerson(request);
    }

}
