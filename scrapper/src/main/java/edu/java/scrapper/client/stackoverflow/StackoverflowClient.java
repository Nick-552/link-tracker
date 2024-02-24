package edu.java.scrapper.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.scrapper.client.AbstractClient;
import edu.java.scrapper.client.LinkUpdatesClient;
import edu.java.scrapper.dto.LastLinkUpdate;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class StackoverflowClient extends AbstractClient implements LinkUpdatesClient {

    protected static final String BASE_URL = "https://api.stackexchange.com/2.3";

    private static final String QUESTION_URI = "/questions/{id}?site=stackoverflow";

    private static final Pattern QUESTION_PATTERN = Pattern
        .compile("^https://stackoverflow.com/questions/(\\d+)(/[\\w-]*)?$");

    @Autowired
    public StackoverflowClient(WebClient.Builder webClientBuilder) {
        this(webClientBuilder, BASE_URL);
        log.info("started");
    }

    public StackoverflowClient(WebClient.Builder webClientBuilder, String baseUrl) {
        super(webClientBuilder, baseUrl);
    }

    @Override
    public LastLinkUpdate getLastUpdate(String urlString) {
        Matcher matcher = QUESTION_PATTERN.matcher(urlString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(NOT_SUPPORTED_EXCEPTION_MESSAGE);
        }
        String questionId = matcher.group(1);
        return new LastLinkUpdate(
            urlString,
            getStackoverflowQuestionInfo(questionId).lastUpdate()
        );
    }

    public StackoverflowQuestionInfo getStackoverflowQuestionInfo(String questionId) {
        var data = webClient.get().uri(QUESTION_URI, questionId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> {
                log.info("http status = error");
                return Mono.empty();
            })
            .bodyToMono(StackoverflowResponse.class)
            .onErrorComplete().block();
        if (data != null && data.items() != null && data.items().length >= 1) {
            return data.items()[0];
        }
        return StackoverflowQuestionInfo.empty();
    }

    @Override
    protected boolean isSupported(String url) {
        return url != null && QUESTION_PATTERN.matcher(url).matches();
    }

    public record StackoverflowResponse(@JsonProperty("items") StackoverflowQuestionInfo[] items) { }

    public record StackoverflowQuestionInfo(
        @JsonProperty("last_activity_date") OffsetDateTime lastUpdate,
        @JsonProperty("answer_count") Integer answerCount
        ) {
        public static StackoverflowQuestionInfo empty() {
            return new StackoverflowQuestionInfo(null, null);
        }
    }
}
