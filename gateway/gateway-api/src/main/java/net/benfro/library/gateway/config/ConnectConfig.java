package net.benfro.library.gateway.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "library.connect")
@PropertySource(value = "classpath:props/connect.properties")
public class ConnectConfig {

    private int userHubRestPort;
}
