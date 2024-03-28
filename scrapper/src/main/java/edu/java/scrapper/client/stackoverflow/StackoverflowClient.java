package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.AbstractJsonWebClient;
import edu.java.scrapper.dto.response.stackoverflow.StackoverflowQuestionResponse;
import edu.java.scrapper.exception.InvalidUrlException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackoverflowClient extends AbstractJsonWebClient {

    private static final String QUESTION_URI = "/questions/%s?site=stackoverflow";

    public StackoverflowClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }

    public <T> T getResponse(String uri, Class<T> tClass) {
        return webClient.get()
            .uri(uri)
            .retrieve()
            .onStatus(
                statusCode -> statusCode.isSameCodeAs(HttpStatus.NOT_FOUND),
                clientResponse -> Mono.error(new InvalidUrlException())
            ).bodyToMono(tClass)
            .block();
    }

    public StackoverflowQuestionResponse getQuestionResponse(String questionId) {
        String questionUri = QUESTION_URI.formatted(questionId);
        return getResponse(questionUri, StackoverflowQuestionResponse.class);
    }
}
