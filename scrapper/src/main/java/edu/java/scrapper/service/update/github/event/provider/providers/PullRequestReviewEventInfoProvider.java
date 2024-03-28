package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import org.springframework.stereotype.Service;

@Service
public class PullRequestReviewEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.PullRequestReviewEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Написал review ")
            .append(payload.review().htmlUrl())
            .append(" для Pull Request ")
            .append("в репозитории ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Статус: ")
            .append(payload.review().state())
            .append("\n")
            .append("Ссылка на Pull Request: ")
            .append(payload.pullRequest().htmlUrl());
        return sb.toString();
    }
}
