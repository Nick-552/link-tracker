package edu.java.bot.configuration;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.handler.UpdateHandlerWithNext;
import edu.java.bot.handler.handlers.terminating.commands.HelpUpdateHandler;
import edu.java.bot.handler.handlers.terminating.commands.ListUpdateHandler;
import edu.java.bot.handler.handlers.terminating.commands.StartUpdateHandler;
import edu.java.bot.handler.handlers.terminating.commands.TrackUpdateHandler;
import edu.java.bot.handler.handlers.terminating.commands.UntrackUpdateHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Command {
    START("start", "зарегистрировать пользователя", new StartUpdateHandler()),
    HELP("help", "вывести окно с командами", new HelpUpdateHandler()),
    TRACK("track", "начать отслеживание ссылки", new TrackUpdateHandler()),
    UNTRACK("untrack", "прекратить отслеживание ссылки", new UntrackUpdateHandler()),
    LIST("list", "показать список отслеживаемых ссылок", new ListUpdateHandler());

    private final String command;

    private final String description;

    private final UpdateHandlerWithNext handler;

    public String command() {
        return command;
    }

    public String description() {
        return description;
    }

    public UpdateHandlerWithNext handler() {
        return handler;
    }

    public BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
