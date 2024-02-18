package edu.java.bot.configuration;

import edu.java.bot.storage.InMemoryUserLinksStorageService;
import edu.java.bot.storage.UserLinksStorageService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {
    private static final UserLinksStorageService STORAGE_SERVICE = new InMemoryUserLinksStorageService();

    public static String about() {
        return "Бот для отслеживания изменений на страницах";
    }

    public static String name() {
        return "LinkTracker";
    }

    @SuppressWarnings("checkstyle:LineLength")
    public static String description() {
        return "С помощью этого бота вы можете отслеживать обновление контента на таких страницах, как stackoverflow и GitHub.";
    }

    public static UserLinksStorageService storage() {
        return STORAGE_SERVICE;
    }
}
