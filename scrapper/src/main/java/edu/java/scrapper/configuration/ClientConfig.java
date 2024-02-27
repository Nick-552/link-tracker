package edu.java.scrapper.configuration;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class ClientConfig {

    private static final String UNAUTHORIZED = "unauthorized";

    @Bean
    public GithubClient githubClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.github.base-url:https://api.github.com}") String baseUrl,
        @Value("${api-client.github.auth-token:}") String githubAuthBearerToken
    ) {
        if (githubAuthBearerToken != null && !githubAuthBearerToken.isBlank()) {
            webClientBuilder.defaultHeader(
                HttpHeaders.AUTHORIZATION,
                "Bearer " + githubAuthBearerToken
            );
            webClientBuilder.baseUrl(baseUrl).build()
                .get().retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.error(new Throwable(UNAUTHORIZED)))
                .bodyToMono(String.class)
                .doOnError(Throwable.class, throwable -> {
                    if (throwable.getMessage().equals(UNAUTHORIZED)) {
                        webClientBuilder.defaultHeaders(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION));
                    }
                }).onErrorComplete().block();
        }
        return new GithubClient(webClientBuilder, baseUrl);
    }

    @Bean
    public StackoverflowClient stackoverflowClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.stackoverflow.base-url:https://api.stackexchange.com/2.3}") String baseUrl
    ) {
        return new StackoverflowClient(webClientBuilder, baseUrl);
    }
}
