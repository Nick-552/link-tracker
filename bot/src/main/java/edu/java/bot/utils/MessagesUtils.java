package edu.java.bot.utils;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessagesUtils {

    public static final String DEFAULT_MESSAGE = """
        Я тебя не совсем понимаю, используй доступные команды.
        Чтобы вывести список доступных команд, используй /help
        """;

    public static final String I_DONT_LIKE_YOUR_UPDATE = """
        Не знаю что ты сделал, но мне это ооооочень не нравится.
        """;

    public static final String NO_SUCH_COMMAND_MESSAGE = "Такой команды нет(";

    public static final String HELP_MESSAGE;

    public static final String NO_LINKS_MESSAGE = "Нет отслеживаемых ссылок";

    private static final String DELIMITER = "\n\n";


    @SuppressWarnings("checkstyle:LineLength")
    private static final String START_TEXT = """
        %s, добро пожаловать в бота LinkTracker!

        Теперь твой аккаунт зарегистрирован в боте и ты сможешь отслеживать изменения контента на сайтах GitHub и StackOverflow.

        Для этого добавь нужные ссылки с помощью команды /track <ссылка для отслеживания>

        Чтобы посмотреть доступные команды, используй /help
        """;

    private static final String TRACK_EXPLANATION = """
        Используйте команду /%1$s в следующем виде:
        /%1$s <ссылка для отслеживания>
        Например:
        /%1$s https://github.com/Nick-552/link-tracker
        """;

    private static final String LINK_ADDED_MESSAGE = "Ссылка для отслеживания добавлена: %s";

    private static final String LINK_REMOVED_MESSAGE = """
        Ссылка для отслеживания удалена: %s
        """;

    private static final String LINKS_LIST = "Список отслеживаемых ссылок:\n\n";

    private static final String INVALID_LINK_MESSAGE = """
        Неверный формат ссылки
        """;

    private static final String ERROR_MESSAGE = "Возникла ошибка:\n%s";

    private static final String LINK_UPDATE_MESSAGE = """
        Обновление ссылки %s
        Обновлена %s
        Подробности:
        %s
        """;

    static {
        StringBuilder sb = new StringBuilder("Доступные команды:\n\n");
        for (var command: Command.values()) {
            sb.append("/%s - %s\n".formatted(command.getCommand(), command.getDescription()));
        }
        HELP_MESSAGE = sb.toString();
    }

    public static String getLinksList(List<String> links) {
        var sb = new StringBuilder(LINKS_LIST);
        for (int i = 0; i < links.size(); i++) {
            sb.append(i + 1).append(". ").append(links.get(i)).append("\n");
        }
        return sb.toString();
    }

    public static String getStartText(String name) {
        return START_TEXT.formatted(name);
    }

    public static String getTrackExplanation(String trackOrUntrack) {
        return TRACK_EXPLANATION.formatted(trackOrUntrack);
    }

    public static String getLinkAddedMessage(String url) {
        return LINK_ADDED_MESSAGE.formatted(url);
    }

    public static String getLinkRemovedMessage(String url) {
        return LINK_REMOVED_MESSAGE.formatted(url);
    }

    public static String getInvalidLinkMessage() {
        return INVALID_LINK_MESSAGE;
    }

    public static SendMessage createErrorMessage(Object chatId, String... messages) {
        var sb = new StringBuilder();
        for (var message: messages) {
            sb.append(message).append(DELIMITER);
        }
        return createMessage(chatId, ERROR_MESSAGE.formatted(sb.toString().strip())).disableWebPagePreview(true);
    }

    public static SendMessage createMessage(Object chatId, String... messages) {
        var sb = new StringBuilder();
        for (var message: messages) {
            sb.append(message).append(DELIMITER);
        }
        return new SendMessage(chatId, sb.toString().strip());
    }

    public static SendMessage createUpdateMessage(
        Object chatId,
        URI link,
        OffsetDateTime offsetDateTime,
        String message
    ) {
        var date = offsetDateTime.toLocalDate();
        var dateString = String.format("%02d.%02d.%s", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        var time = offsetDateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        var offset = offsetDateTime.getOffset();
        var timeString = String.format("%s в %s %s", dateString, time, offset);
        return new SendMessage(chatId, LINK_UPDATE_MESSAGE.formatted(link, timeString, message));
    }
}
