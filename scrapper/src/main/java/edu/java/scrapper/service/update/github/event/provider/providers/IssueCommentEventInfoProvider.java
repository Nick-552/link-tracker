package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class IssueCommentEventInfoProvider implements EventInfoProvider {
    @Override
    public GithubEventType getEventType() {
        return GithubEventType.IssueCommentEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        switch (payload.action()) {
            case "created" -> sb.append("добавил ");
            case "edited" -> sb.append("изменил ");
            case "deleted" -> sb.append("удалил ");
            default -> log.warn("Unknown action: {}", payload.action());
        }
        sb.append("комментарий к issue ")
            .append(payload.issue().htmlUrl());
        sb.append(" в репозитории ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Issue: ")
            .append(payload.issue().body())
            .append("\n")
            .append("Текст комментария: ")
            .append(payload.comment().body());
        return sb.toString();
    }
}
