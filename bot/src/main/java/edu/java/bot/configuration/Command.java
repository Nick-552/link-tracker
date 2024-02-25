package edu.java.bot.configuration;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Command {
    START("start", "зарегистрировать пользователя"),
    HELP("help", "вывести окно с командами"),
    TRACK("track", "начать отслеживание ссылки"),
    UNTRACK("untrack", "прекратить отслеживание ссылки"),
    LIST("list", "показать список отслеживаемых ссылок");

    private final String command;

    private final String description;

    public BotCommand toApiCommand() {
        return new BotCommand(getCommand(), getDescription());
    }
}
