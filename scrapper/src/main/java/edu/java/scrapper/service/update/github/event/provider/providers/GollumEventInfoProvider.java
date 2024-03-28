package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import org.springframework.stereotype.Service;

@Service
public class GollumEventInfoProvider implements EventInfoProvider {
    @Override
    public GithubEventType getEventType() {
        return GithubEventType.GollumEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        return "обновил wiki страницы в репозитории ";
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Обновленные страницы:\n");
        for (var page : payload.pages()) {
            sb.append("\t").append(page.htmlUrl()).append("\n");
        }
        return sb.toString();
    }
}
