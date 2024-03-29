package edu.java.scrapper.client.github;

import edu.java.scrapper.client.AbstractJsonWebClient;
import edu.java.scrapper.dto.response.github.GithubRateResponse;
import edu.java.scrapper.dto.response.github.GithubRepoInfo;
import edu.java.scrapper.exception.InvalidUrlException;
import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GithubClient extends AbstractJsonWebClient {

    private static final String RATE_URI = "/rate_limit";

    private static final String REPO_URI = "/repos/%s/%s";

    private static final String EVENTS_URI = "/repos/%s/%s/events?per_page=%d";

    public GithubClient(
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

    public GithubRepoInfo getRepoResponse(String owner, String repoName) {
        String repoUri = REPO_URI.formatted(owner, repoName);
        return getResponse(repoUri, GithubRepoInfo.class);
    }

    public GithubEvent[] getEventsResponse(String owner, String repoName, int limit) {
        String eventsUri = EVENTS_URI.formatted(owner, repoName, limit);
        return getResponse(eventsUri, GithubEvent[].class);
    }

    public GithubRateResponse getGithubRateInfo() {
        return getResponse(RATE_URI, GithubRateResponse.class);
    }
}
