package net.benfro.library.core.test;

import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import liquibase.integration.spring.SpringLiquibase;
import net.benfro.library.core.LibraryCoreApplication;
import reactor.kafka.sender.SenderOptions;

@SpringBootTest(classes = {LibraryCoreApplication.class, IntegrationTest.TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = IntegrationTest.Initializer.class)
@ExtendWith(ResetExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public interface IntegrationTest {

    class ServiceConfig {

        private static final PostgreSQLContainer postgres = (PostgreSQLContainer) new PostgreSQLContainer(PostgreSQLContainer.IMAGE)
                .waitingFor(Wait.forListeningPort());

        private static final EmbeddedKafkaBroker kafka = new EmbeddedKafkaZKBroker(1);


        @Bean(initMethod = "start", destroyMethod = "close")
        public PostgreSQLContainer postgres() {
            return postgres;
        }

        @Bean(destroyMethod = "destroy")
        public EmbeddedKafkaBroker kafka() {
            return kafka;
        }

        @Bean
//        @Autowired
        public SpringLiquibase liquibase(@Autowired PostgreSQLContainer postgres) {
            var ds = new BasicDataSource();

            ds.setDriverClassName(postgres.getDriverClassName());
            ds.setUrl(postgres.getJdbcUrl());
            ds.setUsername(postgres.getUsername());
            ds.setPassword(postgres.getPassword());

            var liquibase = new SpringLiquibase();

            liquibase.setDataSource(ds);
            liquibase.setChangeLog("classpath:database/changelog.xml");

            return liquibase;
        }
    }

    @Configuration
    class TestConfig {

        @Bean
        public WebTestClient webTestClient(ApplicationContext applicationContext) {
            return WebTestClient.bindToApplicationContext(applicationContext)
                    .configureClient()
                    .build();
        }

        @Bean
        @Autowired
        public ReactiveKafkaProducerTemplate<String, String> producer(EmbeddedKafkaBroker broker) {
            var producerProps = KafkaTestUtils.producerProps(broker);

            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(producerProps));
        }
    }

    class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            var parent = new AnnotationConfigApplicationContext(ServiceConfig.class);
            var postgres = parent.getBean(PostgreSQLContainer.class);
            var kafka = parent.getBean(EmbeddedKafkaBroker.class);

            var config = new MapPropertySource("test-conf", Map.of(
                    "database.host", postgres.getHost(),
                    "database.port", postgres.getFirstMappedPort(),
                    "database.name", postgres.getDatabaseName(),
                    "database.user", postgres.getUsername(),
                    "database.password", postgres.getPassword(),
                    "kafka.bootstrap-servers", kafka.getBrokersAsString()
            ));

            context.getEnvironment().getPropertySources().addFirst(config);
            context.setParent(parent);
        }
    }

}
