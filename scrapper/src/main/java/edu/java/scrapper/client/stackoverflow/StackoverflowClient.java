package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.AbstractJsonWebClient;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient extends AbstractJsonWebClient {

    public StackoverflowClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }
}
