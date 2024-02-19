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
import java.util.Set;

public class ListUpdateHandler extends UpdateHandlerWithNext {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.LIST);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        if (!linksStorageService.isRegistered(user)) {
            return Optional.of(
                new SendMessage(chatID, HandlerMessages.USER_NOT_REGISTERED_YET_MESSAGE)
            );
        }
        Set<String> links = linksStorageService.getLinks(user);
        if (links.isEmpty()) {
            return Optional.of(
                new SendMessage(chatID, HandlerMessages.NO_LINKS_MESSAGE)
            );
        } else {
            StringBuilder sb = new StringBuilder(HandlerMessages.LINKS_LIST);
            for (var link: links) {
                sb.append(link).append("\n");
            }
            return Optional.of(
                new SendMessage(chatID, sb.toString())
            );
        }
    }
}
