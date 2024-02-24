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
    private static final UserLinksStorageService STORAGE_SERVICE = new InMemoryUserLinksStorageService();

    @Bean
    public UserLinksStorageService storage() {
        return STORAGE_SERVICE;
    }
}
