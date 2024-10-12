package net.benfro.library.userhub;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Disabled
class UserHubApplicationTests {

    @Test
    void contextLoads() {
    }

}
