package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.AbstractGetJsonWebClient;
import edu.java.scrapper.client.bot.request.LinkUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BotClient extends AbstractGetJsonWebClient {

    private static final String UPDATES_URI = "/updates";

    public BotClient(
        WebClient.Builder webClientBuilder,
        @Value("${api.bot.base-url:http://localhost:8090}") String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }

    public void sendUpdate(LinkUpdate linkUpdate) {
        webClient.post()
            .uri(UPDATES_URI)
            .body(BodyInserters.fromValue(linkUpdate))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
