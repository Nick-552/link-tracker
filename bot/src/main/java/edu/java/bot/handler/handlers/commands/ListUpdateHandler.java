package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.repository.ChatLinkRepository;
import edu.java.bot.utils.MessagesUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;
import static edu.java.bot.utils.MessagesUtils.createMessage;
import static edu.java.bot.utils.MessagesUtils.getLinksList;

@RequiredArgsConstructor
public class ListUpdateHandler extends UpdateHandlerWithNext {

    private final ChatLinkRepository chatLinkRepository;

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.LIST);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var chatID = HandlerUtils.chatID(update);
        try {
            var links = chatLinkRepository.getLinks(chatID);
            if (links.size() == 0) {
                return Optional.of(
                    createMessage(chatID, MessagesUtils.NO_LINKS_MESSAGE)
                );
            } else {
                var urls = links.links().stream().map(linkResponse -> linkResponse.url().toString()).toList();
                return Optional.of(
                    createMessage(chatID, getLinksList(urls))
                );
            }
        } catch (ScrapperApiException e) {
            return Optional.of(
                createErrorMessage(chatID, e.getApiErrorResponse().description())
            );
        }
    }
}
