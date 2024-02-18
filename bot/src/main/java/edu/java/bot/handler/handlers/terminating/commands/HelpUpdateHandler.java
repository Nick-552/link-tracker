package edu.java.bot.handler.handlers.terminating.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.handlers.terminating.UpdateHandlerTerminating;

public class HelpUpdateHandler extends UpdateHandlerTerminating {

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.HELP);
    }

    @Override
    protected SendMessage doHandle(Update update) {
        return new SendMessage(HandlerUtils.chatID(update), HandlerUtils.HELP_MESSAGE);
    }
}
