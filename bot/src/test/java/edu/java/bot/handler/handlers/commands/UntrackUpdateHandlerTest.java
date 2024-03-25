package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.handlers.HandlerTestUtils;
import edu.java.bot.repository.ChatLinkRepository;
import java.net.URI;
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
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;
import static edu.java.bot.utils.MessagesUtils.createMessage;
import static edu.java.bot.utils.MessagesUtils.getLinkRemovedMessage;
import static edu.java.bot.utils.MessagesUtils.getTrackExplanation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UntrackUpdateHandlerTest {


    @Mock
    ChatLinkRepository storageService = Mockito.mock(ChatLinkRepository.class);

    UntrackUpdateHandler untrackUpdateHandler = new UntrackUpdateHandler(storageService);

    public static Stream<Arguments> supportsSource() {
        return Stream.of(
            Arguments.of("/untrack", true),
            Arguments.of("/UnTrACK", true),
            Arguments.of("/untrack ", true),
            Arguments.of("/untrack smthg", true),
            Arguments.of("/command", false),
            Arguments.of("dhfghfghd", false),
            Arguments.of("untrack", false),
            Arguments.of("/ untrack", false)
        );
    }

    @ParameterizedTest
    @MethodSource("supportsSource")
    void supports_whenHasMessage_shouldSupportOnlyWithUntrackCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = untrackUpdateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenScrapperException_shouldReturnExceptionDescription() {
        String description = "Описание";
        when(storageService.removeLink(any(), any()))
            .thenThrow(new ScrapperApiException(new ApiErrorResponse(
                description,
                HttpStatus.BAD_REQUEST,
                "dfgf",
                "ghdfghdf",
                List.of()
            )));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/track link");
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createErrorMessage(DEFAULT_CHAT_ID, description));
    }

    @Test
    void doHandle_whenInvalidNumberOfTokens_shouldReturnTrackExplanation() {
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/track link smthg");
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID,getTrackExplanation(Command.UNTRACK.getCommand())));
    }

    @Test
    void doHandle_whenLinkSuccessfullyRemoved_shouldReturnLinkNotAddedMessage(){
        var link = "https://link.com";
        when(storageService.removeLink(any(), any())).thenReturn(new LinkResponse(1L, URI.create(link)));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/untrack " + link);
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID, getLinkRemovedMessage(link)));
    }
}
