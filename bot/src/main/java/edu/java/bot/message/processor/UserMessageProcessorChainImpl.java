package edu.java.bot.message.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.HandlerProvider;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.UpdateHandlerLogger;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static edu.java.bot.handler.util.HandlerUtils.chatID;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;

@Service
@Log4j2
public class UserMessageProcessorChainImpl implements UserMessageProcessor {

    private final UpdateHandlerWithNext updateHandler;

    private final Counter counter;

    public UserMessageProcessorChainImpl(@Autowired HandlerProvider provider, MeterRegistry registry) {
        updateHandler = new UpdateHandlerLogger(log);
        updateHandler
            .setNextHandler(provider.getStartUpdateHandler())
            .setNextHandler(provider.getHelpUpdateHandler())
            .setNextHandler(provider.getTrackUpdateHandler())
            .setNextHandler(provider.getUntrackUpdateHandler())
            .setNextHandler(provider.getListUpdateHandler());
        counter = Counter.builder("messages.processed").register(registry);
    }

    @Override
    public AbstractSendRequest<? extends AbstractSendRequest<?>> process(Update update) {
        try {
            counter.increment();
            return updateHandler.handle(update);
        } catch (ScrapperApiException e) {
            return createErrorMessage(
                chatID(update),
                e.getApiErrorResponse().description()
            );
        }
    }
}
