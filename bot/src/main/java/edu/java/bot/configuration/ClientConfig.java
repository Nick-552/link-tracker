package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exception.ScrapperApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
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
        webClientBuilder.filter(createFilter(create(retryConfig.scrapper())))
            .filter(
                ExchangeFilterFunction.ofResponseProcessor(
                    ClientConfig::exchangeFilterResponseProcessor
                )
            );
        return new ScrapperClient(webClientBuilder, baseUrl);
    }

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.isError()) {
            return response.bodyToMono(ApiErrorResponse.class)
                .flatMap(
                    apiErrorResponse -> Mono.error(
                        new ScrapperApiException(apiErrorResponse)
                    )
                );
        }
        return Mono.just(response);
    }
}
