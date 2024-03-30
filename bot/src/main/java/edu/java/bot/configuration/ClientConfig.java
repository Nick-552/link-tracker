package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import static edu.java.bot.utils.retry.RetryFactory.create;
import static edu.java.bot.utils.retry.RetryFactory.createFilter;

@RequiredArgsConstructor
@Configuration
public class ClientConfig {

    private final RetryConfig retryConfig;

    @Bean
    public ScrapperClient scrapperClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.scrapper.base-url}") String baseUrl
    ) {
        webClientBuilder.filter(createFilter(create(retryConfig.scrapper())));
        return new ScrapperClient(webClientBuilder, baseUrl);
    }
}
