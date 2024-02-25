package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.Optional;
import static edu.java.bot.handler.util.HandlerMessages.ALREADY_REGISTERED_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.getStartText;

public class StartUpdateHandler extends UpdateHandlerWithNext {

    private final UserLinksStorageService linksStorageService;

    public StartUpdateHandler(UserLinksStorageService linksStorageService) {
        this.linksStorageService = linksStorageService;
    }

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.START);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        if (linksStorageService.registerUser(user)) {
            return Optional.of(
                new SendMessage(chatID, getStartText(user.firstName()))
            );
        }
        return Optional.of(
            new SendMessage(chatID, ALREADY_REGISTERED_MESSAGE)
        );
    }
}
