package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.HandlerProvider;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.UpdateHandlerLogger;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static edu.java.bot.handler.util.HandlerUtils.chatID;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;

@Service
@Log4j2
public class UserMessageProcessorChainImpl implements UserMessageProcessor {

    private final UpdateHandlerWithNext updateHandler;

    public UserMessageProcessorChainImpl(@Autowired HandlerProvider provider) {
        updateHandler = new UpdateHandlerLogger(log);
        updateHandler
            .setNextHandler(provider.getStartUpdateHandler())
            .setNextHandler(provider.getHelpUpdateHandler())
            .setNextHandler(provider.getTrackUpdateHandler())
            .setNextHandler(provider.getUntrackUpdateHandler())
            .setNextHandler(provider.getListUpdateHandler());
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> process(Update update) {
        try {
            return updateHandler.handle(update);
        } catch (ScrapperApiException e) {
            return createErrorMessage(
                chatID(update),
                e.getApiErrorResponse().description()
            );
        }
    }
}
