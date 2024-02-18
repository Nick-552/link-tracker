package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;

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
}
