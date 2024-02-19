package edu.java.bot.configuration;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.commands.HelpUpdateHandler;
import edu.java.bot.handler.handlers.commands.ListUpdateHandler;
import edu.java.bot.handler.handlers.commands.StartUpdateHandler;
import edu.java.bot.handler.handlers.commands.TrackUpdateHandler;
import edu.java.bot.handler.handlers.commands.UntrackUpdateHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Command {
    START("start", "зарегистрировать пользователя", new StartUpdateHandler()),
    HELP("help", "вывести окно с командами", new HelpUpdateHandler()),
    TRACK("track", "начать отслеживание ссылки", new TrackUpdateHandler()),
    UNTRACK("untrack", "прекратить отслеживание ссылки", new UntrackUpdateHandler()),
    LIST("list", "показать список отслеживаемых ссылок", new ListUpdateHandler());

    private final String command;

    private final String description;

    private final UpdateHandlerWithNext handler;

    public BotCommand toApiCommand() {
        return new BotCommand(getCommand(), getDescription());
    }
}
