package edu.java.scrapper.data.fetcher.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.data.fetcher.AbstractDataFetcher;
import edu.java.scrapper.data.fetcher.LinkUpdatesFetcher;
import edu.java.scrapper.dto.LastLinkUpdate;
import edu.java.scrapper.dto.github.GithubRateInfo;
import edu.java.scrapper.dto.github.GithubRepoInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GithubDataFetcher extends AbstractDataFetcher implements LinkUpdatesFetcher {

    private static final String REPO_URI = "/repos/%s/%s";

    private static final String RATE_URI = "/rate_limit";

    private static final Pattern REPO_PATTERN = Pattern
        .compile("^https://github.com/([\\w-]+)/([\\w-]+)/?$");

    private final GithubClient githubClient;

    public GithubDataFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public GithubRateInfo getRateInfo() throws JSONException, JsonProcessingException {
        var rateJson = githubClient.getJson(RATE_URI)
            .getJSONObject("rate");
        return objectMapper.readValue(rateJson.toString(), GithubRateInfo.class);
    }

    @Override
    public LastLinkUpdate getLastUpdate(String url) throws JSONException, JsonProcessingException {
        Matcher matcher = REPO_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new UnsupportedUrlException();
        }
        var owner = matcher.group(1);
        var repo = matcher.group(2);
        return new LastLinkUpdate(url, getRepoInfo(owner, repo).lastUpdate());
    }

    public GithubRepoInfo getRepoInfo(String owner, String repo) throws JSONException, JsonProcessingException {
        var repoJson = githubClient.getJson(REPO_URI.formatted(owner, repo));
        return objectMapper.readValue(repoJson.toString(), GithubRepoInfo.class);
    }
}
