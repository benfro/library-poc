package net.benfro.library.bookhub;

import org.springframework.boot.SpringApplication;

public class TestUserHubApplication {

	public static void main(String[] args) {
		SpringApplication.from(BookHubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
