package edu.java.bot.configuration;

import edu.java.bot.utils.retry.RetryType;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry")
public record RetryConfig(
    WebClientRetryConfig scrapper
) {
    public record WebClientRetryConfig(
        RetryType retryType,
        int maxRetries,
        Duration initialInterval,
        List<Integer> codes
    ) { }
}
