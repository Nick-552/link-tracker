package edu.java.bot.handler.handlers.continuing;

import com.pengrad.telegrambot.model.Update;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UpdateHandlerLogger extends UpdateHandlerContinuing {

    @Override
    public boolean supports(Update update) {
        return true;
    }

    @Override
    protected void doHandle(Update update) {
        log.info(update.toString());
    }
}
