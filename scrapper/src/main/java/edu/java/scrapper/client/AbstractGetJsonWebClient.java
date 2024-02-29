package edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;

public class AbstractGetJsonWebClient extends AbstractJsonWebClient {

    protected AbstractGetJsonWebClient(WebClient.Builder webClientBuilder, String baseUrl) {
        super(webClientBuilder, baseUrl);
    }

    public <T> T getResponse(String uri, Class<T> tClass) {
        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(tClass)
            .block();
    }
}
