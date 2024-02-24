package edu.java.scrapper.client.github;

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

@Service
@Log4j2
public class GithubClient extends AbstractClient implements LinkUpdatesClient {

    protected static final String BASE_URL = "https://api.github.com";

    private static final String REPO_URI = "/repos/{owner}/{repo-name}";

    private static final Pattern REPO_PATTERN = Pattern
        .compile("^https://github.com/([\\w-]+)/([\\w-]+)/?$");

    @Autowired
    public GithubClient(WebClient.Builder webClientBuilder) {
        this(webClientBuilder, BASE_URL);
        log.info("started");
    }

    public GithubClient(WebClient.Builder webClientBuilder, String baseUrl) {
        super(webClientBuilder, baseUrl);
    }

    @Override
    public LastLinkUpdate getLastUpdate(String url) {
        Matcher matcher = REPO_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(NOT_SUPPORTED_EXCEPTION_MESSAGE);
        }
        var owner = matcher.group(1);
        var repo = matcher.group(2);
        return new LastLinkUpdate(url, getGithubRepoInfo(owner, repo).lastUpdate);
    }

    public GithubRepoInfo getGithubRepoInfo(String owner, String repo) {
        return webClient.get().uri(REPO_URI, owner, repo)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> {
                log.info("http status = error");
                return Mono.empty();
            })
            .bodyToMono(GithubRepoInfo.class)
            .onErrorReturn(new GithubRepoInfo(null, null))
            .block();
    }

    @Override
    protected boolean isSupported(String url) {
        return url != null && REPO_PATTERN.matcher(url).matches();
    }

    public record GithubRepoInfo(
        @JsonProperty("updated_at") OffsetDateTime lastUpdate,
        @JsonProperty("full_name") String name
    ) {
        public static GithubRepoInfo empty() {
            return new GithubRepoInfo(null, null);
        }
    }
}
