package edu.java.bot.handler.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.handler.UpdateHandlerWithNext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class UpdateHandlerLogger extends UpdateHandlerWithNext {

    private final Logger log;

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
