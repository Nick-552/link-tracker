package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;

public interface UserMessageProcessor {

    AbstractSendRequest<? extends AbstractSendRequest<?>> process(Update update);
}
