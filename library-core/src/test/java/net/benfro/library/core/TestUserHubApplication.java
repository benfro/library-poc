package net.benfro.library.core;

import org.springframework.boot.SpringApplication;

public class TestUserHubApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibraryCoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
