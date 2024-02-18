package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;

public interface UserMessageProcessor {

    AbstractSendRequest process(Update update);
}
