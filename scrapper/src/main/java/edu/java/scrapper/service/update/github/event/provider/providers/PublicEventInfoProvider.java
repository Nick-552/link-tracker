package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import org.springframework.stereotype.Service;

@Service
public class PublicEventInfoProvider implements EventInfoProvider {
    @Override
    public GithubEventType getEventType() {
        return GithubEventType.PublicEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        return "Сделал публичным доступ к репозиторию ";
    }
}
