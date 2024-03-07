package edu.java.bot.client.scrapper;

import edu.java.bot.client.AbstractJsonWebClient;
import edu.java.bot.client.scrapper.request.AddLinkRequest;
import edu.java.bot.client.scrapper.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.response.ApiErrorResponse;
import edu.java.bot.client.scrapper.response.LinkResponse;
import edu.java.bot.client.scrapper.response.LinksListResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class ScrapperClient extends AbstractJsonWebClient {

    private static final String TG_CHAT_URI = "/tg-chat/{id}";

    private static final String LINKS_URI = "/links";

    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    private static final String SERVER_ERROR = "Server is not responding";

    public ScrapperClient(
        WebClient.Builder webClientBuilder,
        @Value("${api-client.scrapper.base-url}") String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }

    public void addTgChat(Long chatId) {
        var response = webClient.post()
            .uri(TG_CHAT_URI, chatId).exchangeToMono(clientResponse -> {
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

    public void deleteTgChat(Long chatId) {
        var response = webClient.delete()
            .uri(TG_CHAT_URI, chatId)
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

    public LinksListResponse getLinks(Long chatId) {
        var response = webClient.get()
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful()) {
                    return clientResponse.bodyToMono(LinksListResponse.class);
                } else if (clientResponse.statusCode().is5xxServerError()) {
                    throw new RuntimeException(SERVER_ERROR);
                }
                return clientResponse.bodyToMono(ApiErrorResponse.class);
            }).block();
        if (response instanceof ApiErrorResponse errorResponse) {
            log.info(errorResponse);
            throw new RuntimeException(errorResponse.description());
        }
        return (LinksListResponse) response;
    }

    public LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest) {
        var response = webClient.post()
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(addLinkRequest)
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful()) {
                    return clientResponse.bodyToMono(LinkResponse.class);
                } else if (clientResponse.statusCode().is5xxServerError()) {
                    throw new RuntimeException(SERVER_ERROR);
                }
                return clientResponse.bodyToMono(ApiErrorResponse.class);
            }).block();
        if (response instanceof ApiErrorResponse errorResponse) {
            log.info(errorResponse);
            throw new RuntimeException(errorResponse.description());
        }
        return (LinkResponse) response;
    }

    public LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        var response = webClient.method(HttpMethod.DELETE)
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(removeLinkRequest)
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful()) {
                    return clientResponse.bodyToMono(LinkResponse.class);
                } else if (clientResponse.statusCode().is5xxServerError()) {
                    throw new RuntimeException(SERVER_ERROR);
                }
                return clientResponse.bodyToMono(ApiErrorResponse.class);
            }).block();
        if (response instanceof ApiErrorResponse errorResponse) {
            log.info(errorResponse);
            throw new RuntimeException(errorResponse.description());
        }
        return (LinkResponse) response;
    }
}
