package edu.java.bot.handler;

import edu.java.bot.handler.handlers.commands.HelpUpdateHandler;
import edu.java.bot.handler.handlers.commands.ListUpdateHandler;
import edu.java.bot.handler.handlers.commands.StartUpdateHandler;
import edu.java.bot.handler.handlers.commands.TrackUpdateHandler;
import edu.java.bot.handler.handlers.commands.UntrackUpdateHandler;
import edu.java.bot.repository.ChatLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandlerProvider {

    private final ChatLinkRepository chatLinkRepository;

    public StartUpdateHandler getStartUpdateHandler() {
        return new StartUpdateHandler(chatLinkRepository);
    }

    public HelpUpdateHandler getHelpUpdateHandler() {
        return new HelpUpdateHandler();
    }

    public TrackUpdateHandler getTrackUpdateHandler() {
        return new TrackUpdateHandler(chatLinkRepository);
    }

    public UntrackUpdateHandler getUntrackUpdateHandler() {
        return new UntrackUpdateHandler(chatLinkRepository);
    }

    public ListUpdateHandler getListUpdateHandler() {
        return new ListUpdateHandler(chatLinkRepository);
    }
}
