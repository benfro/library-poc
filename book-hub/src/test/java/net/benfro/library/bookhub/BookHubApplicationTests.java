package net.benfro.library.bookhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import net.benfro.library.bookhub.test.IntegrationTest;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BookHubApplicationTests implements IntegrationTest {

    @Test
    void contextLoads() {
    }

}
