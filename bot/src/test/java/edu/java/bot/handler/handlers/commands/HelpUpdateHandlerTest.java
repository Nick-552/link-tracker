package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.handler.handlers.HandlerTestUtils;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static edu.java.bot.handler.handlers.HandlerTestUtils.DEFAULT_CHAT_ID;
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static edu.java.bot.handler.util.HandlerMessages.HELP_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.createMessage;
import static org.assertj.core.api.Assertions.assertThat;

class HelpUpdateHandlerTest {

    HelpUpdateHandler updateHandler = new HelpUpdateHandler();

    public static Stream<Arguments> supportsSource() {
        return Stream.of(
            Arguments.of("/help", true),
            Arguments.of("/hELp", true),
            Arguments.of("/help ", true),
            Arguments.of("/help smthng", true),
            Arguments.of("/command", false),
            Arguments.of("dhfghfghd", false),
            Arguments.of("help", false)
        );
    }

    @ParameterizedTest
    @MethodSource("supportsSource")
    void supports_whenHasMessage_shouldSupportOnlyWithHelpCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = updateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenSupports_shouldReturnHelpMessage() {
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) updateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID, HELP_MESSAGE));
    }
}
