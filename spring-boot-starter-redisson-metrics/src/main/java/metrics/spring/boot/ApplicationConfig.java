package metrics.spring.boot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.application")
public class ApplicationConfig {

    @Value("${spring.application.name}")
    private String name;

    @Value("${server.address:127.0.0.1}")
    private String host;

    private int port;

    public ApplicationConfig(int port) {
        this.port = port;
    }

    public ApplicationConfig() { }
}
