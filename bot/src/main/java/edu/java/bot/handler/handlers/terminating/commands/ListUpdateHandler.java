package edu.java.bot.handler.handlers.terminating.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.handler.handlers.terminating.UpdateHandlerTerminating;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.Set;

public class ListUpdateHandler extends UpdateHandlerTerminating {

    private final UserLinksStorageService linksStorageService = ApplicationConfig.storage();

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.LIST);
    }

    @Override
    protected SendMessage doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        if (!linksStorageService.isRegistered(user)) {
            return new SendMessage(chatID, HandlerUtils.USER_NOT_REGISTERED_YET_MESSAGE);
        }
        Set<String> links = linksStorageService.getLinks(user);
        if (links.isEmpty()) {
            return new SendMessage(chatID, HandlerUtils.NO_LINKS_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder(HandlerUtils.LINKS_LIST);
            for (var link: links) {
                sb.append(link).append("\n");
            }
            return new SendMessage(chatID, sb.toString());
        }
    }
}
