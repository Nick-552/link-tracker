package edu.java.bot.handler.util;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static edu.java.bot.handler.handlers.HandlerTestUtils.DEFAULT_CHAT_ID;
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static edu.java.bot.handler.util.HandlerMessages.DEFAULT_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.I_DONT_LIKE_YOUR_UPDATE;
import static edu.java.bot.handler.util.HandlerMessages.NO_SUCH_COMMAND_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.createMessage;
import static edu.java.bot.handler.util.HandlerUtils.defaultHandle;
import static org.mockito.ArgumentMatchers.any;

class HandlerUtilsTest {

    @Test
    void defaultHandle_whenCommand_shouldReturnNoSuchCommandMessage() {
        Update update = mockedUpdateWithMessageWithText("/command");
        SendMessage expected = createMessage(DEFAULT_CHAT_ID, NO_SUCH_COMMAND_MESSAGE);
        var actual = defaultHandle(update);
        assertEqualsSendMessages(actual, expected);
    }

    @Test
    void defaultHandle_whenJustText_shouldReturnDefaultMessage() {
        Update update = mockedUpdateWithMessageWithText("just some text");
        SendMessage expected = createMessage(DEFAULT_CHAT_ID, DEFAULT_MESSAGE);
        var actual = defaultHandle(update);
        assertEqualsSendMessages(actual, expected);
    }

    @Test
    void defaultHandle_whenNotAMessageWithText_shouldReturnDontLikeThisUpdate() {
        Update update = new Update();
        try (MockedStatic<HandlerUtils> utilities = Mockito.mockStatic(HandlerUtils.class)) {
            utilities.when(() -> HandlerUtils.chatID(any())).thenReturn(DEFAULT_CHAT_ID);
            utilities.when(() -> HandlerUtils.isMessageAndHasText(any())).thenReturn(false);
            utilities.when(() -> HandlerUtils.defaultHandle(any())).thenCallRealMethod();
            SendMessage expected = createMessage(DEFAULT_CHAT_ID, I_DONT_LIKE_YOUR_UPDATE);
            var actual = defaultHandle(update);
            assertEqualsSendMessages(actual, expected);
        }
    }
}
