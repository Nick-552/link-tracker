package edu.java.scrapper.client.github;

import edu.java.scrapper.client.AbstractGetJsonWebClient;
import edu.java.scrapper.dto.response.github.GithubRateResponse;
import edu.java.scrapper.dto.response.github.GithubRepoInfo;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClient extends AbstractGetJsonWebClient {

    private static final String RATE_URI = "/rate_limit";

    private static final String REPO_URI = "/repos/%s/%s";

    public GithubClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        super(webClientBuilder, baseUrl);
    }

    public GithubRepoInfo getRepoResponse(String owner, String repoName) {
        String repoUri = REPO_URI.formatted(owner, repoName);
        return getResponse(repoUri, GithubRepoInfo.class);
    }

    public GithubRateResponse getGithubRateInfo() {
        return getResponse(RATE_URI, GithubRateResponse.class);
    }
}
