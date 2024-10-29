package net.benfro.library.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class LibraryCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryCoreApplication.class, args);
    }

}
