package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.continuing.UpdateHandlerLogger;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessorChainImpl implements UserMessageProcessor {

    private final UpdateHandlerWithNext updateHandler;

    public UserMessageProcessorChainImpl() {
        updateHandler = new UpdateHandlerLogger();
        updateHandler
            .setNextHandler(Command.START.handler())
            .setNextHandler(Command.HELP.handler())
            .setNextHandler(Command.TRACK.handler())
            .setNextHandler(Command.UNTRACK.handler())
            .setNextHandler(Command.LIST.handler());
    }

    @Override
    public AbstractSendRequest process(Update update) {
        return updateHandler.handle(update);
    }
}
