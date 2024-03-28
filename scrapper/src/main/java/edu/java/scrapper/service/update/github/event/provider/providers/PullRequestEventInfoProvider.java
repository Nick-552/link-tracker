package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PullRequestEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.PullRequestEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        switch (payload.action()) {
            case "opened" -> sb.append("открыл Pull Request ");
            case "closed" -> sb.append("закрыл Pull Request ");
            case "reopened" -> sb.append("заново открыл Pull Request ");
            case "synchronize" -> sb.append("синхронизировал Pull Request ");
            case "edited" -> sb.append("изменил Pull Request ");
            case "assigned" -> sb.append("назначил ответственного для Pull Request ");
            case "unassigned" -> sb.append("удалил ответственного для Pull Request ");
            case "review_requested" -> sb.append("запросил review к Pull Request ");
            case "review_request_removed" -> sb.append("отозвал запрос на review к Pull Request ");
            case "labeled" -> sb.append("добавил ярлык к Pull Request ");
            case "unlabeled" -> sb.append("убрал ярлык к Pull Request ");
            default -> log.warn("Unknown action: " + payload.action());
        }
        sb.append(" в репозитории ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Название Pull Request: ")
            .append(payload.pullRequest().title())
            .append("\n")
            .append("Ссылка на Pull Request: ")
            .append(payload.pullRequest().htmlUrl());
        return sb.toString();
    }
}
