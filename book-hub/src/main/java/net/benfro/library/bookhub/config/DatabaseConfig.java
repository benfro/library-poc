package net.benfro.library.bookhub.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public ConnectionFactory connectionFactory(@Value("${database.host}") String host,
                                               @Value("${database.port}") int port,
                                               @Value("${database.name}") String database,
                                               @Value("${database.user}") String user,
                                               @Value("${database.password}") String password) {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgresql")
                .option(HOST, host)
                .option(PORT, port)
                .option(USER, user)
                .option(PASSWORD, password)
                .option(DATABASE, database)
                .build());
    }
}