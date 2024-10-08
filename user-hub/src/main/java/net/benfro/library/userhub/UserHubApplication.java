package net.benfro.library.userhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
//@ComponentScan(basePackages={"net.benfro.library.userhub"})
@EnableR2dbcRepositories(basePackages = {"net.benfro.library.userhub"})
public class UserHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserHubApplication.class, args);
    }

}
