package edu.java.bot.handler;

import edu.java.bot.handler.handlers.commands.HelpUpdateHandler;
import edu.java.bot.handler.handlers.commands.ListUpdateHandler;
import edu.java.bot.handler.handlers.commands.StartUpdateHandler;
import edu.java.bot.handler.handlers.commands.TrackUpdateHandler;
import edu.java.bot.handler.handlers.commands.UntrackUpdateHandler;
import edu.java.bot.storage.ChatLinksStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandlerProvider {

    private final ChatLinksStorage chatLinksStorage;

    public StartUpdateHandler getStartUpdateHandler() {
        return new StartUpdateHandler(chatLinksStorage);
    }

    public HelpUpdateHandler getHelpUpdateHandler() {
        return new HelpUpdateHandler();
    }

    public TrackUpdateHandler getTrackUpdateHandler() {
        return new TrackUpdateHandler(chatLinksStorage);
    }

    public UntrackUpdateHandler getUntrackUpdateHandler() {
        return new UntrackUpdateHandler(chatLinksStorage);
    }

    public ListUpdateHandler getListUpdateHandler() {
        return new ListUpdateHandler(chatLinksStorage);
    }
}
