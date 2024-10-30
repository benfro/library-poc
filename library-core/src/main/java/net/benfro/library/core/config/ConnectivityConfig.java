package net.benfro.library.core.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "library.connect")
@PropertySource(value = "classpath:props/connect.yml")
public class ConnectivityConfig {

//    @Value("${user-app.port}")
//    public String userAppPort;
//    @Value("${user-app.url:localhost}")
//    public String userAppUrl;

    @NestedConfigurationProperty
    public SomeApp some = new SomeApp();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SomeApp {
//        @Value("${port}")
        public int port;
    }

}
