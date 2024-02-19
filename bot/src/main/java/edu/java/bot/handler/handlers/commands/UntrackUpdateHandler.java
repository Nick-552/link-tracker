package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerMessages;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.Optional;

public class UntrackUpdateHandler extends UpdateHandlerWithNext {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.UNTRACK);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        var user = HandlerUtils.user(update);
        if (!linksStorageService.isRegistered(user)) {
            return Optional.of(
                new SendMessage(chatID, HandlerMessages.USER_NOT_REGISTERED_YET_MESSAGE)
            );
        } else if (tokens.length != 2) {
            return Optional.of(
                new SendMessage(chatID, HandlerMessages.TRACK_EXPLANATION.formatted(Command.UNTRACK.getCommand()))
            );
        }
        String link = tokens[1];
        if (linksStorageService.removeLink(user, link)) {
            return Optional.of(
                new SendMessage(chatID, HandlerMessages.LINK_REMOVED_MESSAGE.formatted(link))
            );
        }
        return Optional.of(
            new SendMessage(chatID, HandlerMessages.NO_SUCH_LINK_MESSAGE.formatted(link))
        );
    }
}
