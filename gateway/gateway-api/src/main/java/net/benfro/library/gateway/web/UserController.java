package net.benfro.library.gateway.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.gateway.api.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/vi/user")
public class UserController {

    private final RSocketRequester rSocketRequester;

    @PostMapping
    public void createUser(User user) {
        log.info("Creating user: {}", user);
    }
}
