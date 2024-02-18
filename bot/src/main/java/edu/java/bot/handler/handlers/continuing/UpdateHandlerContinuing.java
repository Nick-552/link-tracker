package edu.java.bot.handler.handlers.continuing;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.UpdateHandlerWithNext;

public abstract class UpdateHandlerContinuing extends UpdateHandlerWithNext {

    public abstract boolean supports(Update update);

    @Override
    public AbstractSendRequest handle(Update update) {
        if (supports(update)) {
            doHandle(update);
        }
        if (nextHandler != null) {
            return nextHandler.handle(update);
        } else {
            return HandlerUtils.defaultHandle(update);
        }

    }

    abstract protected void doHandle(Update update);
}
