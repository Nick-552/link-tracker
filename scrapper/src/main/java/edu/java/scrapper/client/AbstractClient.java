package edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractClient {

    protected static final String NOT_SUPPORTED_EXCEPTION_MESSAGE =
        "Url isn't supported by this client. Check that isSupported(url) is  true";

    protected final WebClient webClient;

    protected AbstractClient(@Autowired WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    protected abstract boolean isSupported(String url);


}
