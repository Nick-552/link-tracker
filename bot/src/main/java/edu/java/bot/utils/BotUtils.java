package edu.java.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BotUtils {

    public static String about() {
        return "Бот для отслеживания изменений на страницах";
    }

    public static String name() {
        return "LinkTracker";
    }

    @SuppressWarnings("checkstyle:LineLength")
    public static String description() {
        return "С помощью этого бота вы можете отслеживать обновление контента на таких страницах, как stackoverflow и GitHub.";
    }
}
