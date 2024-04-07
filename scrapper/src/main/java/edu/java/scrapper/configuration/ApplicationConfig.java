package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    Scheduler scheduler,
    DatabaseAccessType databaseAccessType,
    List<String> ipWhitelist,
    Boolean useQueue,
    Map<String, String> kafkaTopics
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
