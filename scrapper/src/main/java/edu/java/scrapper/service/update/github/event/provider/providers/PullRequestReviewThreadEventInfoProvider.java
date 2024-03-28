package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PullRequestReviewThreadEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.PullRequestReviewThreadEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        switch (payload.action()) {
            case "resolved" -> sb.append("закрыл ветку в треде");
            case "unresolved" -> sb.append("открыл ветку в треде");
            default -> log.warn("Unknown action: " + payload.action());
        }
        sb.append(payload.thread().htmlUrl())
            .append(" в репозитории ");
        return sb.toString();
    }
}
