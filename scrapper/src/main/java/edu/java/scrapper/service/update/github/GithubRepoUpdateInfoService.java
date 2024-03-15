package edu.java.scrapper.service.update.github;

import edu.java.scrapper.service.fetcher.GithubDataFetcher;
import edu.java.scrapper.service.update.UpdateInfo;
import edu.java.scrapper.service.update.UpdateInfoService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import edu.java.scrapper.model.LinkType;

@RequiredArgsConstructor
public class GithubRepoUpdateInfoService implements UpdateInfoService {

    private final GithubDataFetcher githubDataFetcher;

    @Override
    public OffsetDateTime getLastUpdate(URI url) {
        return githubDataFetcher.getRepoInfo(url).lastUpdate();
    }

    @Override
    public UpdateInfo getUpdateInformation(URI url) {
        return null;
    }

    @Override
    public LinkType linkType() {
        return LinkType.GITHUB_REPO;
    }
}
