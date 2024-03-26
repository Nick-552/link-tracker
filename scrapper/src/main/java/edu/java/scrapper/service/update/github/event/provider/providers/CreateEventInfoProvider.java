package edu.java.scrapper.service.update.github.event.provider.providers;

import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import edu.java.scrapper.service.update.github.event.provider.EventInfoProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CreateEventInfoProvider implements EventInfoProvider {

    @Override
    public GithubEventType getEventType() {
        return GithubEventType.CreateEvent;
    }

    @Override
    public String action(GithubEvent.Payload payload) {
        var sb = new StringBuilder();
        sb.append("создал ");
        switch (payload.refType()) {
            case "branch" -> sb.append("ветку ")
                .append(payload.ref());
            case "tag" -> sb.append("тег ")
                .append(payload.ref());
            default -> log.warn("Unknown ref type: " + payload.refType());
        }
        sb.append(" в репозитории ");
        return sb.toString();
    }
}
