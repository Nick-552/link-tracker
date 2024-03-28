package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import org.springframework.stereotype.Service;

@Service
public class ReleaseEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.ReleaseEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        return "создал релиз в репозитории ";
    }

    @Override
    public String additionalInfo(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("Версия: ").append(payload.release().name()).append("\n")
            .append("Описание: ").append(payload.release().body()).append("\n")
            .append("Ссылка: ").append(payload.release().htmlUrl());
        return sb.toString();
    }
}
