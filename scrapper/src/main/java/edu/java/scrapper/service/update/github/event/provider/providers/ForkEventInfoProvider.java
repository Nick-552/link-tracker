package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;

public class ForkEventInfoProvider implements EventInfoProvider {
    @Override
    public GithubEventType getEventType() {
        return GithubEventType.ForkEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        return "форкнул репозиторий ";
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Ссылка на forkee-репозиторий: ")
            .append(payload.forkee().htmlUrl());
        return sb.toString();
    }
}
