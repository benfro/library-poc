package net.benfro.library.gateway;

import org.springframework.boot.SpringApplication;

public class TestGatewayApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(GatewayApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
