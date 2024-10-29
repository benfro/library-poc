package net.benfro.library.core.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("library.connect")
@PropertySource("classpath:props/connect.yaml")
public class ConnectivityConfig {

    @Value("${user-app.port}")
    public String userAppPort;
    @Value("${user-app.url:localhost}")
    public String userAppUrl;

    @NestedConfigurationProperty
    public SomeApp someApp = new SomeApp();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SomeApp {
        public String port;
    }

}
