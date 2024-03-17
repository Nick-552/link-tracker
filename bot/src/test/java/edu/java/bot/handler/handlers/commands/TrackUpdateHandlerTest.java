package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.dto.request.scrapper.AddLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import edu.java.bot.exception.ScrapperApiException;
import edu.java.bot.handler.handlers.HandlerTestUtils;
import edu.java.bot.storage.ChatLinksStorage;
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
import static edu.java.bot.utils.MessagesUtils.getLinkAddedMessage;
import static edu.java.bot.utils.MessagesUtils.getTrackExplanation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrackUpdateHandlerTest {


    @Mock
    ChatLinksStorage storageService = Mockito.mock(ChatLinksStorage.class);

    TrackUpdateHandler trackUpdateHandler = new TrackUpdateHandler(storageService);

    public static Stream<Arguments> supportsSource() {
        return Stream.of(
            Arguments.of("/track", true),
            Arguments.of("/TrACK", true),
            Arguments.of("/track ", true),
            Arguments.of("/track smthg", true),
            Arguments.of("/command", false),
            Arguments.of("dhfghfghd", false),
            Arguments.of("track", false),
            Arguments.of("/ track", false)
        );
    }

    @ParameterizedTest
    @MethodSource("supportsSource")
    void supports_whenHasMessage_shouldSupportOnlyWithTrackCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = trackUpdateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenScrapperException_shouldReturnExceptionDescription() {
        String description = "Описание";
        when(storageService.addLink(any(), any()))
            .thenThrow(new ScrapperApiException(new ApiErrorResponse(
                description,
                HttpStatus.BAD_REQUEST,
                "dfgf",
                "ghdfghdf",
                List.of()
            )));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/track link");
        SendMessage actual = (SendMessage) trackUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createErrorMessage(
            DEFAULT_CHAT_ID,
            description,
            getTrackExplanation(Command.TRACK.getCommand())
        ));
    }

    @Test
    void doHandle_whenInvalidNumberOfTokens_shouldReturnTrackExplanation() {
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/track link smthg");
        SendMessage actual = (SendMessage) trackUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID, getTrackExplanation(Command.TRACK.getCommand())));
    }

    @Test
    void doHandle_whenLinkValidAndAdded_shouldReturnLinkAddedMessage() {
        var link = "https://link.com";
        when(storageService.addLink(DEFAULT_CHAT_ID, new AddLinkRequest(URI.create(link))))
            .thenReturn(new LinkResponse(1L, URI.create(link)));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/track " + link);
        SendMessage actual = (SendMessage) trackUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID, getLinkAddedMessage(link)));
    }
}
