package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.UpdateHandlerLogger;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProcessorChainImpl implements UserMessageProcessor {

    private final UpdateHandlerWithNext updateHandler;

    public UserMessageProcessorChainImpl() {
        updateHandler = new UpdateHandlerLogger();
        updateHandler
            .setNextHandler(Command.START.getHandler())
            .setNextHandler(Command.HELP.getHandler())
            .setNextHandler(Command.TRACK.getHandler())
            .setNextHandler(Command.UNTRACK.getHandler())
            .setNextHandler(Command.LIST.getHandler());
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> process(Update update) {
        return updateHandler.handle(update);
    }
}
