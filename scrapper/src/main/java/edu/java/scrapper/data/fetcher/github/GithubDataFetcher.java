package edu.java.scrapper.data.fetcher.github;

import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.data.fetcher.AbstractDataFetcher;
import edu.java.scrapper.data.fetcher.LinkUpdatesFetcher;
import edu.java.scrapper.dto.LastLinkUpdate;
import edu.java.scrapper.dto.response.github.GithubRateInfo;
import edu.java.scrapper.dto.response.github.GithubRepoInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class GithubDataFetcher extends AbstractDataFetcher implements LinkUpdatesFetcher {

    private static final Pattern REPO_PATTERN = Pattern
        .compile("^https://github.com/([\\w-]+)/([\\w-]+)/?$");

    private final GithubClient githubClient;

    public GithubDataFetcher(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public GithubRateInfo getRateInfo() {
        return githubClient.getGithubRateInfo().rateInfo();
    }

    @Override
    public LastLinkUpdate getLastUpdate(String url) {
        Matcher matcher = REPO_PATTERN.matcher(url);
        if (matcher.matches()) {
            var owner = matcher.group(1);
            var repo = matcher.group(2);
            return new LastLinkUpdate(url, getRepoInfo(owner, repo).lastUpdate());
        }
        throw new UnsupportedUrlException();
    }

    public GithubRepoInfo getRepoInfo(String owner, String repo) {
        return githubClient.getRepoResponse(owner, repo);
    }
}
