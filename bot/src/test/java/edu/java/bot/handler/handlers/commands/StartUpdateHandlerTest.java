package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.handler.handlers.HandlerTestUtils;
import edu.java.bot.handler.util.HandlerMessages;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import static edu.java.bot.handler.handlers.HandlerTestUtils.DEFAULT_USER_FNAME;
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.createSendMessage;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static edu.java.bot.handler.util.HandlerMessages.getStartText;
import static edu.java.bot.handler.util.HandlerUtils.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class StartUpdateHandlerTest {

    StartUpdateHandler startUpdateHandler = new StartUpdateHandler();

    @Mock
    static UserLinksStorageService storageService = Mockito.mock(UserLinksStorageService.class);

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(startUpdateHandler, "linksStorageService", storageService);
    }

    public static Stream<Arguments> supportsSource() {
        return Stream.of(
            Arguments.of("/start", true),
            Arguments.of("/StARt", true),
            Arguments.of("/start ", true),
            Arguments.of("/start smthng", true),
            Arguments.of("/command", false),
            Arguments.of("dhfghfghd", false),
            Arguments.of("start", false)
        );
    }

    @ParameterizedTest
    @MethodSource("supportsSource")
    void supports_whenHasMessage_shouldSupportOnlyWithListCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = startUpdateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenRegistered_shouldReturnAlreadyRegisteredMessage() {
        when(storageService.registerUser(any())).thenReturn(false);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) startUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        Mockito.verify(storageService).registerUser(user(update));
        assertEqualsSendMessages(actual, createSendMessage(HandlerMessages.ALREADY_REGISTERED_MESSAGE));
    }

    @Test
    void doHandle_whenNotRegistered_shouldReturnStartMessage() {
        when(storageService.registerUser(any())).thenReturn(true);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) startUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        Mockito.verify(storageService).registerUser(user(update));
        assertEqualsSendMessages(actual, createSendMessage(getStartText(DEFAULT_USER_FNAME)));
    }
}
