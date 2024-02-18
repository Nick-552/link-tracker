package edu.java.bot.handler.handlers.terminating;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.UpdateHandlerWithNext;

public abstract class UpdateHandlerTerminating extends UpdateHandlerWithNext {

    public abstract boolean supports(Update update);

    @Override
    public AbstractSendRequest handle(Update update) {
        if (supports(update)) {
            return doHandle(update);
        } else if (nextHandler != null) {
            return nextHandler.handle(update);
        } else {
            return HandlerUtils.defaultHandle(update);
        }

    }

    abstract protected AbstractSendRequest doHandle(Update update);
}
