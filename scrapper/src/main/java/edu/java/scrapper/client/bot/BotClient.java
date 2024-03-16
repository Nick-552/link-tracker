package edu.java.scrapper.client.bot;

import edu.java.scrapper.client.AbstractJsonWebClient;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BotClient extends AbstractJsonWebClient {

    private static final String UPDATES_URI = "/updates";

    public BotClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.bot.base-url}") String baseUrl
    ) {
        super(
            webClientBuilder.filter(
                ExchangeFilterFunction.ofResponseProcessor(
                    BotClient::exchangeFilterResponseProcessor
                )
            ), baseUrl
        );
    }

    public Void sendUpdate(LinkUpdate linkUpdate) {
        return webClient.post()
            .uri(UPDATES_URI)
            .bodyValue(linkUpdate)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.isError()) {
            return response.bodyToMono(ApiErrorResponse.class)
                .flatMap(apiErrorResponse -> Mono.error(
                    new ApiException(
                        apiErrorResponse.code(),
                        apiErrorResponse.description(),
                        apiErrorResponse.exceptionMessage()
                    )
                ));
        }
        return Mono.just(response);
    }
}
