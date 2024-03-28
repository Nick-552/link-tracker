package edu.java.bot.handler.handlers;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class HandlerTestUtils {

    public static final long DEFAULT_CHAT_ID = 1;
    public static final String DEFAULT_USER_FNAME = "Nick";

    public static Update mockedUpdateWithMessageWithText(String text) {
        Chat chat = Mockito.mock(Chat.class);
        Message message = Mockito.mock(Message.class);
        Update update = Mockito.mock(Update.class);
        User user = Mockito.mock(User.class);
        when(message.text()).thenReturn(text);
        when(chat.id()).thenReturn(DEFAULT_CHAT_ID);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);
        when(user.firstName()).thenReturn(DEFAULT_USER_FNAME);
        when(update.message().from()).thenReturn(user);
        return update;
    }

    public static void assertEqualsSendMessages(SendMessage actual, SendMessage expected) {
        assertThat(actual.getParameters().get("text"))
            .isEqualTo(expected.getParameters().get("text"));
        assertThat(actual.getParameters().get("chat_id"))
            .isEqualTo(expected.getParameters().get("chat_id"));
    }
}
