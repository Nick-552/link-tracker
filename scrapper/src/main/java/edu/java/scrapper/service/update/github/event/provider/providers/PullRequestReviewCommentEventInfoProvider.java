package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PullRequestReviewCommentEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.PullRequestReviewCommentEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        switch (payload.action()) {
            case "created" -> sb.append("добавил новый ");
            case "edited" -> sb.append("отредактировал ");
            case "deleted" -> sb.append("удалил ");
            default -> log.warn("Unknown action: " + payload.action());
        }
        sb.append("комментарий к review ")
            .append(payload.review().htmlUrl())
            .append(" в Pull Request ")
            .append(payload.pullRequest().htmlUrl())
            .append(" в репозитории ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Текст комментария: ")
            .append(payload.comment().body())
            .append("\n")
            .append("Ссылка на комментарий: ")
            .append(payload.comment().htmlUrl());
        return sb.toString();
    }
}
