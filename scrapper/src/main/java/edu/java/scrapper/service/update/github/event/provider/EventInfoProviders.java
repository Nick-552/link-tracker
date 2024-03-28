package edu.java.scrapper.service.update.github.event.provider;

import edu.java.scrapper.service.update.github.event.model.GithubEventType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EventInfoProviders {

    private static final Map<GithubEventType, EventInfoProvider> EVENT_INFO_PROVIDERS = new HashMap<>();

    public EventInfoProvider getForType(GithubEventType githubEventType) {
        return EVENT_INFO_PROVIDERS.get(githubEventType);
    }

    public void register(GithubEventType githubEventType, EventInfoProvider eventInfoProvider) {
        EVENT_INFO_PROVIDERS.put(githubEventType, eventInfoProvider);
    }
}
