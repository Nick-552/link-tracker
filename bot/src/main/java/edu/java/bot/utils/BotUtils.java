package edu.java.bot.utils;

import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BotUtils {

    public static final String ABOUT = "Бот для отслеживания изменений на страницах";

    public static final String NAME = "LinkTracker";

    @SuppressWarnings("checkstyle:LineLength")
    public static final String DESCRIPTION = "С помощью этого бота вы можете отслеживать обновление контента на таких страницах, как stackoverflow и GitHub.";

    public static final String UPDATE = """
        Обновление ссылки %s
        Обновлена %s
        Подробности:
        %s
        """;

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
        return new SendMessage(chatId, UPDATE.formatted(link, timeString, message));
    }
}
