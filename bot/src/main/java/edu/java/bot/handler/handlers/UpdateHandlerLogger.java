package edu.java.bot.handler.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.handler.UpdateHandlerWithNext;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UpdateHandlerLogger extends UpdateHandlerWithNext {

    @Override
    public boolean supports(Update update) {
        return true;
    }

    @Override
    protected Optional<AbstractSendRequest<? extends AbstractSendRequest<?>>> doHandle(Update update) {
        log.info(update.toString());
        return Optional.empty();
    }
}
