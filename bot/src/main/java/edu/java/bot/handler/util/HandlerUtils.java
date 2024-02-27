package edu.java.bot.handler.util;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HandlerUtils {

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
        return isCommand(update) && (text(update).toLowerCase().startsWith("/" + command.getCommand() + " ")
            || text(update).equalsIgnoreCase("/" + command.getCommand()));
    }

    public static SendMessage defaultHandle(Update update) {
        if (isCommand(update)) {
            return new SendMessage(chatID(update), HandlerMessages.NO_SUCH_COMMAND_MESSAGE);
        } else if (isMessageAndHasText(update)) {
            return new SendMessage(chatID(update), HandlerMessages.DEFAULT_MESSAGE);
        }
        return new SendMessage(chatID(update), HandlerMessages.I_DONT_LIKE_YOUR_UPDATE);
    }
}
