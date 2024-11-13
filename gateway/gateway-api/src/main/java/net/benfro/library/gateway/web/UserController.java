package net.benfro.library.gateway.web;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.gateway.api.NewUserRequest;
import net.benfro.library.gateway.api.UserDTO;
import net.benfro.library.gateway.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new User")
    public Mono<UserDTO> createUser(@RequestBody NewUserRequest newUserRequest) {
        final UserDTO personToCreate = UserDTO.builder()
                .email(newUserRequest.getEmail())
                .firstName(newUserRequest.getFirstName())
                .lastName(newUserRequest.getLastName())
                .build();

        return userService.createUser(personToCreate);
    }
}
