package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.dto.request.scrapper.AddLinkRequest;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.storage.ChatLinksStorage;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.bot.handler.util.HandlerMessages.createErrorMessage;
import static edu.java.bot.handler.util.HandlerMessages.createMessage;
import static edu.java.bot.handler.util.HandlerMessages.getInvalidLinkMessage;
import static edu.java.bot.handler.util.HandlerMessages.getLinkAddedMessage;
import static edu.java.bot.handler.util.HandlerMessages.getTrackExplanation;

@RequiredArgsConstructor
public class TrackUpdateHandler extends UpdateHandlerWithNext {

    private final ChatLinksStorage chatLinksStorage;

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.TRACK);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var chatID = HandlerUtils.chatID(update);
        var tokens = HandlerUtils.text(update).split(" ");
        if (tokens.length != 2) {
            return Optional.of(
                createMessage(chatID, getTrackExplanation(Command.TRACK.getCommand()))
            );
        }
        try {
            chatLinksStorage.addLink(chatID, new AddLinkRequest(URI.create(tokens[1])));
            return Optional.of(
                createMessage(chatID, getLinkAddedMessage(tokens[1]))
            );
        } catch (ScrapperApiException e) {
            return Optional.of(
                createErrorMessage(
                    chatID,
                    e.getApiErrorResponse().description(),
                    getTrackExplanation(Command.TRACK.getCommand())
                )
            );
        } catch (IllegalArgumentException e) {
            return Optional.of(
                createErrorMessage(
                    chatID,
                    getInvalidLinkMessage()
                )
            );
        }
    }
}
