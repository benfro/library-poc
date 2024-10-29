package net.benfro.library.core.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ConnectivityConfig.class})
class ConnectivityConfigTest {

    @Autowired
    ConnectivityConfig instance;

    @Test
    void testGetProps() {
        assertEquals("8000", instance.getUserAppPort());
        //assertEquals(9000, instance.someApp.getPort());
    }
}
