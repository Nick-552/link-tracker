package edu.java.bot.client.scrapper.scrapper;

import edu.java.bot.client.scrapper.AbstractJsonWebClient;
import edu.java.bot.client.scrapper.scrapper.request.AddLinkRequest;
import edu.java.bot.client.scrapper.scrapper.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.scrapper.response.LinkResponse;
import edu.java.bot.client.scrapper.scrapper.response.LinksListResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Log4j2
public class ScrapperClient extends AbstractJsonWebClient {

    private static final String TG_CHAT_URI = "/tg-chat/{id}";

    private static final String LINKS_URI = "/links";

    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    public ScrapperClient(
        WebClient.Builder webClientBuilder,
        @Value("${api.scrapper.base-url:http://localhost:8080}") String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
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
        return webClient.post()
            .uri(LINKS_URI)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
