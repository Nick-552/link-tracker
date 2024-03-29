package edu.java.scrapper.service.fetcher;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.dto.response.github.GithubRateInfo;
import edu.java.scrapper.dto.response.github.GithubRepoInfo;
import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubDataFetcher {

    private static final Pattern REPO_PATTERN = Pattern
        .compile("^https://github.com/([\\w-]+)/([\\w-]+)/?$");

    private final GithubClient githubClient;

    public GithubRateInfo getRateInfo() {
        return githubClient.getGithubRateInfo().rateInfo();
    }


    public GithubRepoInfo getRepoInfo(URI url) {
        Matcher matcher = REPO_PATTERN.matcher(url.toString());
        if (matcher.matches()) {
            var owner = matcher.group(1);
            var repo = matcher.group(2);
            return githubClient.getRepoResponse(owner, repo);
        }
        throw new UnsupportedUrlException();
    }

    public Stream<GithubEvent> getEvents(URI url, int limit) {
        Matcher matcher = REPO_PATTERN.matcher(url.toString());
        if (matcher.matches()) {
            var owner = matcher.group(1);
            var repo = matcher.group(2);
            return Arrays.stream(githubClient.getEventsResponse(owner, repo, limit));
        }
        throw new UnsupportedUrlException();
    }
}
