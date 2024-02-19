package edu.java.bot.handler.util;

import edu.java.bot.configuration.Command;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HandlerMessages {

    public static final String DEFAULT_MESSAGE = """
        Я тебя не совсем понимаю, используй доступные команды.
        Чтобы вывести список доступных команд, используй /help
        """;

    public static final String I_DONT_LIKE_YOUR_UPDATE = """
        Не знаю что ты сделал, но мне это ооооочень не нравится.
        """;

    public static final String NO_SUCH_COMMAND_MESSAGE = "Такой команды нет(";

    public static final String HELP_MESSAGE;

    public static final String USER_NOT_REGISTERED_YET_MESSAGE = "Пользователь еще не зарегистрирован";

    public static final String NO_LINKS_MESSAGE = "Нет отслеживаемых ссылок";

    public static final String LINKS_LIST = "Список отслеживаемых ссылок:\n\n";

    @SuppressWarnings("checkstyle:LineLength")
    public static final String START_TEXT = """
        %s, добро пожаловать в бота LinkTracker!

        Теперь твой аккаунт зарегистрирован в боте и ты сможешь отслеживать изменения контента на сайтах GitHub и StackOverflow.

        Для этого добавь нужные ссылки с помощью команды /track <ссылка для отслеживания>

        Чтобы посмотреть доступные команды, используй /help
        """;

    public static final String ALREADY_REGISTERED_MESSAGE = """
        Пользователь уже зарегистрирован или при регистрации возникла ошибка
        """;

    public static final String TRACK_EXPLANATION = """
        Используйте команду /%1$s в следующем виде:
        /%1$s <ссылка для отслеживания>
        Например:
        /%1$s https://github.com/Nick-552/link-tracker
        """;

    public static final String INVALID_LINK_MESSAGE = "Ссылка имеет некорректный вид: %s";

    public static final String LINK_ADDED_MESSAGE = "Ссылка для отслеживания добавлена: %s";

    public static final String LINK_ALREADY_ADDED_MESSAGE = "Эта ссылка уже добавлена в лист отслеживаемых ссылок: %s";

    public static final String LINK_REMOVED_MESSAGE = """
        Ссылка для отслеживания удалена: %s
        """;

    public static final String NO_SUCH_LINK_MESSAGE = """
        Такой ссылки нет в списке отслеживаемых или возникла ошибка при удалении ссылки из списка: %s
        """;

    static {
        StringBuilder sb = new StringBuilder("Доступные команды:\n\n");
        for (var command: Command.values()) {
            sb.append("/%s - %s\n".formatted(command.getCommand(), command.getDescription()));
        }
        HELP_MESSAGE = sb.toString();
    }
}
