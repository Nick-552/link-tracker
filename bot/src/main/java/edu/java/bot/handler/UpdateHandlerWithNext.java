package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.handler.util.HandlerUtils;
import java.util.Optional;

public abstract class UpdateHandlerWithNext implements UpdateHandler {

    protected UpdateHandler nextHandler;

    public UpdateHandlerWithNext setNextHandler(UpdateHandlerWithNext nextHandler) {
        this.nextHandler = nextHandler;
        return (UpdateHandlerWithNext) this.nextHandler;
    }

    public UpdateHandler setNextHandler(UpdateHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this.nextHandler;
    }

    public abstract boolean supports(Update update);

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> handle(Update update) {
        if (supports(update)) {
            return doHandle(update).orElse(nextHandle(update));
        }
        return nextHandle(update);
    }

    protected AbstractSendRequest<? extends AbstractSendRequest<?>> nextHandle(Update update) {
        if (nextHandler != null) {
            return nextHandler.handle(update);
        }
        return HandlerUtils.defaultHandle(update);
    }

    abstract protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update);
}
