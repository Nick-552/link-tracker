package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import org.springframework.stereotype.Service;

@Service
public class CommitCommentEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.CommitCommentEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("оставил комментарий к коммиту ")
            .append(payload.comment().htmlUrl())
            .append(" в репозитории ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Текст комментария: ")
            .append(payload.comment().body());
        return sb.toString();
    }
}
