package edu.java.scrapper.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
public abstract class AbstractJsonWebClient {

    protected final WebClient webClient;

    protected AbstractJsonWebClient(@Autowired WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
        log.info("created");
    }

    public JSONObject getJson(String uri) throws JSONException {
        return new JSONObject(webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .block());
    }
}
