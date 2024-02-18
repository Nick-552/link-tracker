package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.request.SetMyDescription;
import com.pengrad.telegrambot.request.SetMyName;
import com.pengrad.telegrambot.request.SetMyShortDescription;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.HandlerUtils;
import edu.java.bot.message.processor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LinkTrackerBot extends TelegramBot implements Bot {

    private final ApplicationConfig config;

    private final UserMessageProcessor userMessageProcessor;

    public LinkTrackerBot(ApplicationConfig config, UserMessageProcessor userMessageProcessor) {
        super(config.telegramToken());
        this.config = config;
        this.userMessageProcessor = userMessageProcessor;
        log.info("bot created");
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (HandlerUtils.chatID(update) != -1) {
                AbstractSendRequest sendRequest = userMessageProcessor.process(update);
                this.execute(sendRequest);
            } else {
                log.info("Can't get chatID");
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    @PostConstruct
    public void start() {
        setUpdatesListener(this);
        execute(
            new SetMyCommands(
                Arrays.stream(Command.values())
                    .map(Command::toApiCommand)
                    .toArray(BotCommand[]::new)
            )
        );
        execute(new SetMyName().name(ApplicationConfig.name()));
        execute(new SetMyShortDescription().description(ApplicationConfig.about()));
        execute(new SetMyDescription().description(ApplicationConfig.description()));
        log.info("bot started and configured");
    }

    @Override
    public void close() {
        this.shutdown();
    }
}
