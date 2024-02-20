package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.UpdateHandlerLogger;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserMessageProcessorChainImpl implements UserMessageProcessor {

    private final UpdateHandlerWithNext updateHandler;

    public UserMessageProcessorChainImpl() {
        updateHandler = new UpdateHandlerLogger(log);
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
