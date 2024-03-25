package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerUtils;
import edu.java.bot.repository.ChatLinkRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;
import static edu.java.bot.utils.MessagesUtils.createMessage;
import static edu.java.bot.utils.MessagesUtils.getStartText;

@RequiredArgsConstructor
public class StartUpdateHandler extends UpdateHandlerWithNext {

    private final ChatLinkRepository chatLinkRepository;

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.START);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        var user = HandlerUtils.user(update);
        var chatID = HandlerUtils.chatID(update);
        try {
            chatLinkRepository.addTgChat(chatID);
            return Optional.of(
                createMessage(chatID, getStartText(user.firstName()))
            );
        } catch (ScrapperApiException e) {
            return Optional.of(
                createErrorMessage(chatID, e.getApiErrorResponse().description())
            );
        }
    }
}
