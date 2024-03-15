package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerMessages;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.ChatLinksStorage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.bot.handler.util.HandlerMessages.createErrorMessage;
import static edu.java.bot.handler.util.HandlerMessages.createMessage;
import static edu.java.bot.handler.util.HandlerMessages.getLinksList;

@RequiredArgsConstructor
public class ListUpdateHandler extends UpdateHandlerWithNext {

    private final ChatLinksStorage chatLinksStorage;

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.LIST);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var chatID = HandlerUtils.chatID(update);
        try {
            var links = chatLinksStorage.getLinks(chatID);
            if (links.size() == 0) {
                return Optional.of(
                    createMessage(chatID, HandlerMessages.NO_LINKS_MESSAGE)
                );
            } else {
                var linksList = links.links().stream().map(linkResponse -> linkResponse.url().toString()).toList();
                return Optional.of(
                    createMessage(chatID, getLinksList(linksList))
                );
            }
        } catch (ScrapperApiException e) {
            return Optional.of(
                createErrorMessage(chatID, e.getApiErrorResponse().description())
            );
        }
    }
}
