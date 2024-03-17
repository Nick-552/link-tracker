package edu.java.scrapper.service.update.github;

import edu.java.scrapper.model.LinkType;
import edu.java.scrapper.service.fetcher.GithubDataFetcher;
import edu.java.scrapper.service.update.UpdateInfo;
import edu.java.scrapper.service.update.UpdateInfoService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubRepoUpdateInfoService implements UpdateInfoService {

    private final GithubDataFetcher githubDataFetcher;

    @Override
    public OffsetDateTime getLastUpdate(URI url) {
        return githubDataFetcher.getRepoInfo(url).lastPush();
    }

    @Override
    public UpdateInfo getUpdateInformation(URI url) {
        return new UpdateInfo("Новый push", getLastUpdate(url));
    }

    @Override
    public LinkType linkType() {
        return LinkType.GITHUB_REPO;
    }
}
