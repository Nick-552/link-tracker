package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.client.AbstractGetJsonWebClient;
import edu.java.scrapper.dto.response.stackoverflow.StackoverflowQuestionResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class StackoverflowClient extends AbstractGetJsonWebClient {

    private static final String QUESTION_URI = "/questions/%s?site=stackoverflow";

    public StackoverflowClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }

    public StackoverflowQuestionResponse getQuestionResponse(String questionId) {
        String questionUri = QUESTION_URI.formatted(questionId);
        return getResponse(questionUri, StackoverflowQuestionResponse.class);
    }
}
