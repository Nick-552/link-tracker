package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface UpdateHandler {

    AbstractSendRequest<? extends AbstractSendRequest<?>> handle(Update update);
}
