package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.dto.request.scrapper.RemoveLinkRequest;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.ChatLinksStorage;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.bot.handler.util.HandlerMessages.createErrorMessage;
import static edu.java.bot.handler.util.HandlerMessages.createMessage;
import static edu.java.bot.handler.util.HandlerMessages.getLinkRemovedMessage;
import static edu.java.bot.handler.util.HandlerMessages.getTrackExplanation;
import static edu.java.bot.handler.util.HandlerUtils.isCommand;

@RequiredArgsConstructor
public class UntrackUpdateHandler extends UpdateHandlerWithNext {

    private final ChatLinksStorage chatLinksStorage;

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
            );
        }
        String link = tokens[1];
        try {
            chatLinksStorage.removeLink(chatID, new RemoveLinkRequest(URI.create(link)));
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
