package edu.java.bot.client.scrapper;

import edu.java.bot.client.AbstractJsonWebClient;
import edu.java.bot.dto.request.scrapper.AddLinkRequest;
import edu.java.bot.dto.request.scrapper.RemoveLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import edu.java.bot.dto.response.scrapper.LinksListResponse;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.repository.ChatLinkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ScrapperClient extends AbstractJsonWebClient implements ChatLinkRepository {

    private static final String TG_CHAT_URI = "/tg-chat/{id}";

    private static final String LINKS_URI = "/links";

    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    public ScrapperClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.scrapper.base-url}") String baseUrl
    ) {
        super(
            webClientBuilder.filter(
                ExchangeFilterFunction.ofResponseProcessor(
                    ScrapperClient::exchangeFilterResponseProcessor
                )
            ), baseUrl
        );
    }

    public Void addTgChat(Long chatId) {
        return webClient.post()
            .uri(TG_CHAT_URI, chatId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public Void deleteTgChat(Long chatId) {
        return webClient.delete()
            .uri(TG_CHAT_URI, chatId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public LinksListResponse getLinks(Long chatId) {
        return webClient.get()
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .retrieve()
            .bodyToMono(LinksListResponse.class)
            .block();
    }

    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.isError()) {
            return response.bodyToMono(ApiErrorResponse.class)
                .flatMap(
                    apiErrorResponse -> Mono.error(
                        new ScrapperApiException(apiErrorResponse)
                    )
                );
        }
        return Mono.just(response);
    }
}
