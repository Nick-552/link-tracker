package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import org.springframework.stereotype.Service;

@Service
public class PushEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.PushEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("запушил в ветку ")
            .append(payload.ref())
            .append(" в репозиторий ");
        return sb.toString();
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Коммиты:\n");
        for (var commit : payload.commits()) {
            sb.append(commit.message()).append("\n");
        }
        return sb.toString();
    }
}
