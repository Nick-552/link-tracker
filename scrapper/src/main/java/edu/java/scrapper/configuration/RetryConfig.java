package edu.java.scrapper.configuration;

import edu.java.scrapper.util.retry.RetryType;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry")
public record RetryConfig(
    WebClientRetryConfig bot,
    WebClientRetryConfig github,
    WebClientRetryConfig stackoverflow
) {
    public record WebClientRetryConfig(
        RetryType retryType,
        int maxRetries,
        Duration initialInterval,
        List<Integer> codes
    ) { }
}
