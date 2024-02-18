package edu.java.bot.handler.handlers.terminating.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.handlers.terminating.UpdateHandlerTerminating;
import edu.java.bot.storage.UserLinksStorageService;

public class StartUpdateHandler extends UpdateHandlerTerminating {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.START);
    }

    @Override
    protected SendMessage doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        if (linksStorageService.registerUser(user)) {
            return new SendMessage(chatID, HandlerUtils.START_TEXT.formatted(user.firstName()));
        }
        return new SendMessage(chatID, HandlerUtils.ALREADY_REGISTERED_MESSAGE);
    }
}
