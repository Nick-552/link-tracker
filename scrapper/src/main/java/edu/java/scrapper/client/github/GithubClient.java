package edu.java.scrapper.client.github;

import edu.java.scrapper.client.AbstractJsonWebClient;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient extends AbstractJsonWebClient {

    public GithubClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }
}
