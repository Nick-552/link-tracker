package edu.java.bot.configuration;

import edu.java.bot.storage.InMemoryUserLinksStorageService;
import edu.java.bot.storage.UserLinksStorageService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {

    @Bean
    public UserLinksStorageService storage() {
        return new InMemoryUserLinksStorageService();
    }
}
