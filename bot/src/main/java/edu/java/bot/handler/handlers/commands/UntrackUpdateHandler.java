package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.dto.request.scrapper.RemoveLinkRequest;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.repository.ChatLinkRepository;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.bot.handler.util.HandlerUtils.isCommand;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;
import static edu.java.bot.utils.MessagesUtils.createMessage;
import static edu.java.bot.utils.MessagesUtils.getLinkRemovedMessage;
import static edu.java.bot.utils.MessagesUtils.getTrackExplanation;

@RequiredArgsConstructor
public class UntrackUpdateHandler extends UpdateHandlerWithNext {

    private final ChatLinkRepository chatLinkRepository;

    @Override
    public boolean supports(Update update) {
        return isCommand(update, Command.UNTRACK);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        long chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        User user = HandlerUtils.user(update);
        if (tokens.length != 2) {
            return Optional.of(
                createMessage(chatID, getTrackExplanation(Command.UNTRACK.getCommand()))
                    .disableWebPagePreview(true)
            );
        }
        String link = tokens[1];
        try {
            chatLinkRepository.removeLink(chatID, new RemoveLinkRequest(URI.create(link)));
            return Optional.of(
                createMessage(chatID, getLinkRemovedMessage(link))
            );
        } catch (ScrapperApiException e) {
            return Optional.of(
                createErrorMessage(chatID, e.getApiErrorResponse().description())
            );
        }
    }
}
