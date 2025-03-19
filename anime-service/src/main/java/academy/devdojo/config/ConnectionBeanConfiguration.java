package academy.devdojo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties configurationProperties;
//    @Profile("mysql")
    @Bean
    public Connection connectionMySql() {
        return new Connection(configurationProperties.url(), configurationProperties.username(), configurationProperties.password());
    }

    @Bean
    @Profile("mongo")
    public Connection connectionMongo() {
//        return new Connection(url, username, password);
        return null;
    }
}
