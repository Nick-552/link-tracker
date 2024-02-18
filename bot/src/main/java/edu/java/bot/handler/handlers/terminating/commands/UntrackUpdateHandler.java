package edu.java.bot.handler.handlers.terminating.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.handlers.terminating.UpdateHandlerTerminating;
import edu.java.bot.storage.UserLinksStorageService;

public class UntrackUpdateHandler extends UpdateHandlerTerminating {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.UNTRACK);
    }

    @Override
    protected AbstractSendRequest doHandle(Update update) {
        var chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        var user = HandlerUtils.user(update);
        if (!linksStorageService.isRegistered(user)) {
            return new SendMessage(HandlerUtils.chatID(update), HandlerUtils.USER_NOT_REGISTERED_YET_MESSAGE);
        } else if (tokens.length != 2) {
            return new SendMessage(chatID, HandlerUtils.TRACK_EXPLANATION.formatted(Command.UNTRACK.command()));
        }
        String link = tokens[1];
        if (linksStorageService.removeLink(user, link)) {
            return new SendMessage(chatID, HandlerUtils.LINK_REMOVED_MESSAGE.formatted(link));
        }
        return new SendMessage(chatID, HandlerUtils.NO_SUCH_LINK_MESSAGE.formatted(link));
    }
}
