package edu.java.bot.handler.handlers.terminating.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.handlers.terminating.UpdateHandlerTerminating;
import edu.java.bot.storage.UserLinksStorageService;

public class TrackUpdateHandler extends UpdateHandlerTerminating {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();


    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.TRACK);
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    protected AbstractSendRequest doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        if (!linksStorageService.isRegistered(user)) {
            return new SendMessage(chatID, HandlerUtils.USER_NOT_REGISTERED_YET_MESSAGE);
        } else if (tokens.length != 2) {
            return new SendMessage(chatID, HandlerUtils.TRACK_EXPLANATION.formatted(Command.TRACK.command()));
        }
        var link = tokens[1];
        if (!isValid(link)) {
            return new SendMessage(chatID, HandlerUtils.INVALID_LINK_MESSAGE.formatted(link));
        }
        if (linksStorageService.addLink(user, tokens[1])) {
            return new SendMessage(chatID, HandlerUtils.LINK_ADDED_MESSAGE.formatted(link));
        }
        return new SendMessage(chatID, HandlerUtils.LINK_ALREADY_ADDED_MESSAGE.formatted(link));
    }

    private static boolean isValid(String url) {
        return url.startsWith("https://") || url.startsWith("http://");
    }
}
