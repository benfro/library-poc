package net.benfro.library.bookhub.integration.gutendex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Service
public class PreRunner implements CommandLineRunner {

//    @Autowired
    WebClientService webClientService;

    @Override
    public void run(String... args) throws Exception {
        webClientService.findUsers()
            .doOnNext(e -> log.info("users -> {}", e))
            .subscribe();
    }
}
