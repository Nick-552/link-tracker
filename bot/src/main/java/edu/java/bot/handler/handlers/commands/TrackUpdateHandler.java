package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.Optional;
import static edu.java.bot.handler.util.HandlerMessages.USER_NOT_REGISTERED_YET_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.getInvalidLinkMessage;
import static edu.java.bot.handler.util.HandlerMessages.getLinkAddedMessage;
import static edu.java.bot.handler.util.HandlerMessages.getLinkNotAddedMessage;
import static edu.java.bot.handler.util.HandlerMessages.getTrackExplanation;
import static edu.java.bot.utils.LinkUtils.isHttpLink;

public class TrackUpdateHandler extends UpdateHandlerWithNext {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();


    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.TRACK);
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        if (!linksStorageService.isRegistered(user)) {
            return Optional.of(
                new SendMessage(chatID, USER_NOT_REGISTERED_YET_MESSAGE)
            );
        } else if (tokens.length != 2) {
            return Optional.of(
                new SendMessage(chatID, getTrackExplanation(Command.TRACK.getCommand()))
            );
        }
        var link = tokens[1];
        if (!isHttpLink(link)) {
            return Optional.of(
                new SendMessage(chatID, getInvalidLinkMessage(link))
            );
        }
        if (linksStorageService.addLink(user, tokens[1])) {
            return Optional.of(
                new SendMessage(chatID, getLinkAddedMessage(link))
            );
        }
        return Optional.of(
            new SendMessage(chatID, getLinkNotAddedMessage(link))
        );
    }
}
