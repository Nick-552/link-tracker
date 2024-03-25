package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import edu.java.bot.dto.response.scrapper.LinksListResponse;
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
import static edu.java.bot.utils.MessagesUtils.NO_LINKS_MESSAGE;
import static edu.java.bot.utils.MessagesUtils.createErrorMessage;
import static edu.java.bot.utils.MessagesUtils.createMessage;
import static edu.java.bot.utils.MessagesUtils.getLinksList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ListUpdateHandlerTest {

    @Mock
    ChatLinkRepository storageService = Mockito.mock(ChatLinkRepository.class);

    ListUpdateHandler listUpdateHandler = new ListUpdateHandler(storageService);

    public static Stream<Arguments> supportsSource() {
        return Stream.of(
            Arguments.of("/list", true),
            Arguments.of("/lISt", true),
            Arguments.of("/list ", true),
            Arguments.of("/list smthng", true),
            Arguments.of("/command", false),
            Arguments.of("dhfghfghd", false),
            Arguments.of("list", false)
        );
    }

    @ParameterizedTest
    @MethodSource("supportsSource")
    void supports_whenHasMessage_shouldSupportOnlyWithListCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = listUpdateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenScrapperException_shouldReturnExceptionDescription() {
        String description = "Описание";
        when(storageService.getLinks(any()))
            .thenThrow(new ScrapperApiException(new ApiErrorResponse(
                description,
                HttpStatus.BAD_REQUEST,
                "dfgf",
                "ghdfghdf",
                List.of()
            )));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) listUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createErrorMessage(DEFAULT_CHAT_ID, description));
    }

    @Test
    void doHandle_whenEmptyList_shouldReturnNoLinksMessage() {
        when(storageService.getLinks(any())).thenReturn(new LinksListResponse(List.of(), 0));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) listUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(actual, createMessage(DEFAULT_CHAT_ID, NO_LINKS_MESSAGE));
    }

    @Test
    void doHandle_whenRegisteredAndHasTrackedLinks_shouldReturnListOfLinks() {
        var linkResponses = List.of(
            new LinkResponse(1L, URI.create("http://google.com")),
            new LinkResponse(2L, URI.create("http://yandex.ru"))
        );
        when(storageService.getLinks(any()))
            .thenReturn(new LinksListResponse(linkResponses, 2));
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) listUpdateHandler.doHandle(update)
            .orElse(createMessage(DEFAULT_CHAT_ID));
        assertEqualsSendMessages(
            actual,
            createMessage(
                DEFAULT_CHAT_ID,
                getLinksList(
                    linkResponses.stream()
                        .map(linkResponse -> linkResponse.url().toString())
                        .toList()
                )
            )
        );
    }
}
