package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.handlers.HandlerTestUtils;
import edu.java.bot.storage.ChatLinksStorage;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import static edu.java.bot.handler.handlers.HandlerTestUtils.DEFAULT_CHAT_ID;
import static edu.java.bot.handler.handlers.HandlerTestUtils.DEFAULT_USER_FNAME;
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;
import static edu.java.bot.utils.MessagesUtils.createMessage;
import static edu.java.bot.utils.MessagesUtils.getStartText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class StartUpdateHandlerTest {

    @Mock
    ChatLinksStorage storageService = Mockito.mock(ChatLinksStorage.class);

    StartUpdateHandler startUpdateHandler = new StartUpdateHandler(storageService);

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
    void supports_whenHasMessage_shouldSupportOnlyWithStartCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = startUpdateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenScrapperException_shouldReturnExceptionDescription() {
        String description = "Описание";
        when(storageService.addTgChat(any()))
            .thenThrow(new ScrapperApiException(new ApiErrorResponse(
                description,
                HttpStatus.BAD_REQUEST,
                "dfgf",
                "ghdfghdf",
                List.of()
            )));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) startUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createErrorMessage(DEFAULT_CHAT_ID, description));
    }

    @Test
    void doHandle_whenNotRegistered_shouldReturnStartMessage() {
        doNothing().when(storageService).addTgChat(any());
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) startUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID, getStartText(DEFAULT_USER_FNAME)));
    }
}
