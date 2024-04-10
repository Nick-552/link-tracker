package edu.java.bot.handler.util;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import edu.java.bot.configuration.Command;
import edu.java.bot.utils.MessagesUtils;
import lombok.experimental.UtilityClass;
import static edu.java.bot.utils.MessagesUtils.createMessage;

@UtilityClass
public class HandlerUtils {

    public static String text(Update update) {
        return update.message().text().toLowerCase();
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
        return isMessage(update) && update.message().text() != null;
    }

    public static boolean isCommand(Update update) {
        return isMessageAndHasText(update) && text(update).startsWith("/");
    }

    public static boolean isCommand(Update update, Command command) {
        return isCommand(update) && (text(update).startsWith("/" + command.getCommand() + " ")
            || text(update).equalsIgnoreCase("/" + command.getCommand()));
    }

    public static AbstractSendRequest<? extends AbstractSendRequest<?>> defaultHandle(Update update) {
        if (isCommand(update)) {
            return createMessage(chatID(update), MessagesUtils.NO_SUCH_COMMAND_MESSAGE);
        } else if (isMessageAndHasText(update)) {
            return createMessage(chatID(update), MessagesUtils.DEFAULT_MESSAGE);
        }
        return createMessage(chatID(update), MessagesUtils.I_DONT_LIKE_YOUR_UPDATE);
    }
}
