package edu.java.bot.provider;

import edu.java.bot.handler.handlers.commands.HelpUpdateHandler;
import edu.java.bot.handler.handlers.commands.ListUpdateHandler;
import edu.java.bot.handler.handlers.commands.StartUpdateHandler;
import edu.java.bot.handler.handlers.commands.TrackUpdateHandler;
import edu.java.bot.handler.handlers.commands.UntrackUpdateHandler;
import edu.java.bot.storage.UserLinksStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandlerProvider {

    private final UserLinksStorageService storageService;

    public StartUpdateHandler getStartUpdateHandler() {
        return new StartUpdateHandler(storageService);
    }

    public HelpUpdateHandler getHelpUpdateHandler() {
        return new HelpUpdateHandler();
    }

    public TrackUpdateHandler getTrackUpdateHandler() {
        return new TrackUpdateHandler(storageService);
    }

    public UntrackUpdateHandler getUntrackUpdateHandler() {
        return new UntrackUpdateHandler(storageService);
    }

    public ListUpdateHandler getListUpdateHandler() {
        return new ListUpdateHandler(storageService);
    }
}
