package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.AbstractJsonWebClient;
import edu.java.scrapper.client.bot.request.LinkUpdate;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class BotClient extends AbstractJsonWebClient {

    private static final String UPDATES_URI = "/updates";

    private static final String SERVER_ERROR = "Server is not responding";

    public BotClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.bot.base-url}") String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }

    public void sendUpdate(LinkUpdate linkUpdate) {
        var response = webClient.post()
            .uri(UPDATES_URI)
            .bodyValue(linkUpdate)
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful()) {
                    return Mono.empty();
                } else if (clientResponse.statusCode().is5xxServerError()) {
                    throw new RuntimeException(SERVER_ERROR);
                }
                return clientResponse.bodyToMono(ApiErrorResponse.class);
            }).block();
        if (response instanceof ApiErrorResponse errorResponse) {
            log.info(errorResponse);
            throw new RuntimeException(errorResponse.description());
        }
    }
}
