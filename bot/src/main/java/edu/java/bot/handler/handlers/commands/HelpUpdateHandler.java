package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.util.HandlerMessages;
import edu.java.bot.handler.util.HandlerUtils;
import java.util.Optional;
import static edu.java.bot.handler.util.HandlerMessages.createMessage;

public class HelpUpdateHandler extends UpdateHandlerWithNext {

    @Override
    public boolean supports(Update update) {
        return HandlerUtils.isCommand(update, Command.HELP);
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        return Optional.of(
            createMessage(HandlerUtils.chatID(update), HandlerMessages.HELP_MESSAGE)
        );
    }
}
