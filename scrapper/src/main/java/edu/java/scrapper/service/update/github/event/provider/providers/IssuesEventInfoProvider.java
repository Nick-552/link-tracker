package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class IssuesEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.IssuesEvent;
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals") @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        switch (payload.action()) {
            case "opened" -> sb.append("открыл ");
            case "edited" -> sb.append("изменил ");
            case "closed" -> sb.append("закрыл ");
            case "reopened" -> sb.append("переоткрыл ");
            case "assigned" -> sb.append("назначил ")
                .append(payload.assignee().login())
                .append(" на ");
            case "unassigned" -> sb.append("снял назначение с ")
                .append(payload.assignee().login())
                .append(" для ");
            case "labeled" -> sb.append("повесил label ")
                .append(payload.label().name()).append(":").append(payload.label().description())
                .append(" на ");
            case "unlabeled" -> sb.append("снял label ")
                .append(payload.label().name()).append(":").append(payload.label().description())
                .append(" с ");
            default -> log.warn("Unknown action: {}", payload.action());
        }
        sb.append("issue ")
            .append(payload.issue().htmlUrl())
            .append(" в репозитории ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Issue: ").append(payload.issue().body());
        return sb.toString();
    }
}
