package edu.java.bot.handler.handlers;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class HandlerTestUtils {

    public static final long DEFAULT_CHAT_ID = 1;

    public static Update mockedUpdateWithMessageWithText(String text) {
        Chat chat = Mockito.mock(Chat.class);
        Message message = Mockito.mock(Message.class);
        Update update = Mockito.mock(Update.class);
        Mockito.when(message.text()).thenReturn(text);
        Mockito.when(chat.id()).thenReturn(DEFAULT_CHAT_ID);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(update.message()).thenReturn(message);
        return update;
    }

    public static SendMessage createSendMessage(String message) {
        return new SendMessage(DEFAULT_CHAT_ID, message);
    }

    public static void assertEqualsSendMessages(SendMessage m1, SendMessage m2) {
        assertThat(m1.getParameters().get("text"))
            .isEqualTo(m2.getParameters().get("text"));
        assertThat(m1.getParameters().get("chat_id"))
            .isEqualTo(m2.getParameters().get("chat_id"));
    }
}
