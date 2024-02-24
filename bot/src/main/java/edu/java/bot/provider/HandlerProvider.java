package edu.java.bot.provider;

import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.commands.HelpUpdateHandler;
import edu.java.bot.handler.handlers.commands.ListUpdateHandler;
import edu.java.bot.handler.handlers.commands.StartUpdateHandler;
import edu.java.bot.handler.handlers.commands.TrackUpdateHandler;
import edu.java.bot.handler.handlers.commands.UntrackUpdateHandler;
import edu.java.bot.storage.UserLinksStorageService;
import org.springframework.stereotype.Component;

@Component
public class HandlerProvider {

    private final UserLinksStorageService storageService;

    public UpdateHandlerWithNext getStartUpdateHandler() {
        return new StartUpdateHandler(storageService);
    }

    public UpdateHandlerWithNext getHelpUpdateHandler() {
        return new HelpUpdateHandler();
    }

    public HandlerProvider(UserLinksStorageService storageService) {
        this.storageService = storageService;
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
