package edu.java.scrapper.service.update.github.event.provider;

import edu.java.scrapper.service.update.EventInfo;
import edu.java.scrapper.service.update.github.event.model.GithubEvent;
import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import org.springframework.beans.factory.annotation.Autowired;

public interface EventInfoProvider {

    GithubEventType getEventType();

    default EventInfo getEventInfo(GithubEvent githubEvent) {
        return new EventInfo(githubEvent.getCreatedAt(), getEventDescription(githubEvent));
    }

    default String getEventDescription(GithubEvent githubEvent) {
        var payload = githubEvent.getPayload();
        StringBuilder sb = new StringBuilder();
        sb.append("Пользователь ")
            .append(githubEvent.getActor().login()) // actor
            .append(" ");
        sb.append(action(payload)); // action
        sb.append(githubEvent.getRepo().fullName()); // repo
        sb.append("\n");
        sb.append(additionalInfo(payload)); // additional info
        return sb.toString();
    }

    String action(GithubEvent.Payload payload);

    default String additionalInfo(GithubEvent.Payload payload) {
        return "";
    }

    @Autowired
    default void init(EventInfoProviders eventInfoProviders) {
        eventInfoProviders.register(getEventType(), this);
    }
}
