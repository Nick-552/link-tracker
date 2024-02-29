package edu.java.scrapper.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
public abstract class AbstractJsonWebClient {

    protected final WebClient webClient;

    protected AbstractJsonWebClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
        log.info("created");
    }

    public <T> T getResponse(String uri, Class<T> tClass) {
        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(tClass)
            .block();
    }
}
