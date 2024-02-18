package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HandlerUtils {

    private static final String DEFAULT_MESSAGE = """
        Я тебя не совсем понимаю, используй доступные команды.
        Чтобы вывести список доступных команд, используй /help
        """;

    private static final String I_DONT_LIKE_YOUR_UPDATE = """
        Не знаю что ты сделал, но мне это ооооочень не нравится.
        """;

    private static final String NO_SUCH_COMMAND_MESSAGE = "Такой команды нет(";

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
            sb.append("/%s - %s\n".formatted(command.command(), command.description()));
        }
        HELP_MESSAGE = sb.toString();
    }

    public static String text(Update update) {
        return update.message().text();
    }

    public static long chatID(Update update) {
        if (isMessage(update)) {
            return update.message().chat().id();
        } else if (update.editedMessage() != null) {
            return update.editedMessage().chat().id();
        }
        return -1;
    }

    public static User user(Update update) {
        return update.message().from();
    }

    public static boolean isMessage(Update update) {
        return update.message() != null;
    }

    public static boolean isMessageAndHasText(Update update) {
        return isMessage(update) && text(update) != null;
    }

    public static boolean isCommand(Update update) {
        return isMessageAndHasText(update) && text(update).startsWith("/");
    }

    public static boolean isCommand(Update update, Command command) {
        return isCommand(update) && (text(update).toLowerCase().startsWith("/" + command.command() + " ")
            || text(update).equalsIgnoreCase("/" + command.command()));
    }

    public static SendMessage defaultHandle(Update update) {
        if (isCommand(update)) {
            return new SendMessage(chatID(update), NO_SUCH_COMMAND_MESSAGE);
        } else if (isMessageAndHasText(update)) {
            return new SendMessage(chatID(update), DEFAULT_MESSAGE);
        }
        return new SendMessage(chatID(update), I_DONT_LIKE_YOUR_UPDATE);
    }
}
