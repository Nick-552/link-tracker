package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.handler.util.HandlerMessages;
import edu.java.bot.handler.handlers.HandlerTestUtils;
import edu.java.bot.storage.UserLinksStorageService;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.createSendMessage;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ListUpdateHandlerTest {

    ListUpdateHandler listUpdateHandler = new ListUpdateHandler();

    @Mock
    static UserLinksStorageService storageService = Mockito.mock(UserLinksStorageService.class);

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(listUpdateHandler, "linksStorageService", storageService);
    }

    public static Stream<Arguments> supportsSource() {
        return Stream.of(
            Arguments.of("/list", true),
            Arguments.of("/lISt", true),
            Arguments.of("/list ", true),
            Arguments.of("/list smthng", true),
            Arguments.of("/command", false),
            Arguments.of("dhfghfghd", false),
            Arguments.of("help", false)
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
    void doHandle_whenNotRegistered_shouldReturnNotRegisteredMessage() {
        when(storageService.isRegistered(any())).thenReturn(false);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) listUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        assertEqualsSendMessages(actual, createSendMessage(HandlerMessages.USER_NOT_REGISTERED_YET_MESSAGE));
    }

    @Test
    void doHandle_whenEmptyList_shouldReturnNoLinksMessage() {
        when(storageService.isRegistered(any())).thenReturn(true);
        when(storageService.getLinks(any())).thenReturn(Set.of());
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) listUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        assertEqualsSendMessages(actual, createSendMessage(HandlerMessages.NO_LINKS_MESSAGE));
    }

    @Test
    void doHandle_whenRegisteredAndHasTrackedLinks_shouldReturnListOfLinks() {
        Set<String> links = Set.of("link1", "link2");
        when(storageService.isRegistered(any())).thenReturn(true);
        when(storageService.getLinks(any())).thenReturn(links);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) listUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        StringBuilder sb = new StringBuilder(HandlerMessages.LINKS_LIST);
        for (var link: links) {
            sb.append(link).append("\n");
        }
        assertEqualsSendMessages(actual, createSendMessage(sb.toString()));
    }
}
