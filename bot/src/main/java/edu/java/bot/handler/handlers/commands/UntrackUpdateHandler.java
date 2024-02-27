package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.Optional;
import static edu.java.bot.handler.util.HandlerMessages.USER_NOT_REGISTERED_YET_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.getLinkRemovedMessage;
import static edu.java.bot.handler.util.HandlerMessages.getNoSuchLinkMessage;
import static edu.java.bot.handler.util.HandlerMessages.getTrackExplanation;
import static edu.java.bot.handler.util.HandlerUtils.isCommand;

public class UntrackUpdateHandler extends UpdateHandlerWithNext {

    private final UserLinksStorageService linksStorageService;

    public UntrackUpdateHandler(UserLinksStorageService linksStorageService) {
        this.linksStorageService = linksStorageService;
    }

    @Override
    public boolean supports(Update update) {
        return isCommand(update, Command.UNTRACK);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        long chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        User user = HandlerUtils.user(update);
        if (!linksStorageService.isRegistered(user)) {
            return Optional.of(
                new SendMessage(chatID, USER_NOT_REGISTERED_YET_MESSAGE)
            );
        } else if (tokens.length != 2) {
            return Optional.of(
                new SendMessage(chatID, getTrackExplanation(Command.UNTRACK.getCommand()))
            );
        }
        String link = tokens[1];
        if (linksStorageService.removeLink(user, link)) {
            return Optional.of(
                new SendMessage(chatID, getLinkRemovedMessage(link))
            );
        }
        return Optional.of(
            new SendMessage(chatID, getNoSuchLinkMessage(link))
        );
    }
}
