package edu.java.scrapper.service.update.github;

import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.model.LinkType;
import edu.java.scrapper.service.fetcher.GithubDataFetcher;
import edu.java.scrapper.service.update.EventInfo;
import edu.java.scrapper.service.update.UpdateInfo;
import edu.java.scrapper.service.update.UpdateInfoService;
import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProviders;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubRepoUpdateInfoService implements UpdateInfoService {

    private final GithubDataFetcher githubDataFetcher;

    private final EventInfoProviders eventInfoProviders;

    private static final int MAX_EVENTS = 10;

    @Override
    public OffsetDateTime getLastUpdate(URI url) {
        return githubDataFetcher.getEvents(url, 1)
            .findFirst()
            .map(GithubEvent::getCreatedAt)
            .orElseThrow(UnsupportedUrlException::new);
    }

    @Override
    public UpdateInfo getUpdateInformation(URI url, OffsetDateTime after) {
        var eventsStream = githubDataFetcher.getEvents(url, MAX_EVENTS)
            .filter(githubEvent -> githubEvent.getCreatedAt().isAfter(after));
        return createUpdateInformation(eventsStream);
    }

    @Override
    public LinkType linkType() {
        return LinkType.GITHUB_REPO;
    }

    public UpdateInfo createUpdateInformation(Stream<GithubEvent> events) {
        var eventsInfos = events.map(event -> {
            var eventInfoProvider = eventInfoProviders.getForType(event.getType());
            if (eventInfoProvider != null) {
                return eventInfoProvider.getEventInfo(event);
            }
            return new EventInfo(event.getCreatedAt(), "Неизвестное событие");
        }).toList();
        return new UpdateInfo(eventsInfos);
    }
}
