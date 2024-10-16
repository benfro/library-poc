package net.benfro.library.userhub;

import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.Arrays;

@SpringBootApplication
//@ComponentScan(basePackages={"net.benfro.library.userhub"})
@EnableR2dbcRepositories(basePackages = {"net.benfro.library.userhub.repository"})
public class UserHubApplication {

    @Import({DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class})
    static class RunLiquibase {

        @EventListener(ApplicationReadyEvent.class)
        public void doSomethingAfterStartup() {
            System.out.println("hello world, I have just started up");
        }


    }

    public static void main(String[] args) {
        if(Arrays.stream(args).anyMatch(s -> s.equals("dbinit"))) {
            new SpringApplicationBuilder(RunLiquibase.class)
            .contextFactory(ApplicationContextFactory.ofContextClass(AnnotationConfigApplicationContext.class))
                    .run(args);
        }

        SpringApplication.run(UserHubApplication.class, args);
    }

}
