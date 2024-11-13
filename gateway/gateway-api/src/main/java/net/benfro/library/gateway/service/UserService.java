package net.benfro.library.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.gateway.api.UserDTO;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RSocketRequester rSocketRequester;

    public Mono<UserDTO> createUser(UserDTO newUserRequest) {
        log.info("Creating user: {}", newUserRequest);
        return rSocketRequester
                .route("createUser")
                .data(newUserRequest)
                .retrieveMono(UserDTO.class);
    }

}
