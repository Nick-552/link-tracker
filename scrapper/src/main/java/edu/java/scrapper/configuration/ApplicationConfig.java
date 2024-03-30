package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    Scheduler scheduler,
    DatabaseAccessType databaseAccessType
) {
    public record Scheduler(
        boolean enable,
        @NotNull Duration interval,
        @NotNull Duration initialDelay,
        @NotNull Duration forceCheckDelay,
        @NotNull Integer checkLimit) {
    }

    public enum DatabaseAccessType {
        JDBC, JOOQ, JPA
    }
}
