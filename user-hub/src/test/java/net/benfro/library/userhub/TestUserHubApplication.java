package net.benfro.library.userhub;

import org.springframework.boot.SpringApplication;

public class TestUserHubApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserHubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
