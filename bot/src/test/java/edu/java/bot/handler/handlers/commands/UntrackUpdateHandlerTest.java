package edu.java.bot.handler.handlers.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.Command;
import edu.java.bot.handler.handlers.HandlerTestUtils;
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
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.handlers.HandlerTestUtils.createSendMessage;
import static edu.java.bot.handler.handlers.HandlerTestUtils.mockedUpdateWithMessageWithText;
import static edu.java.bot.handler.util.HandlerMessages.USER_NOT_REGISTERED_YET_MESSAGE;
import static edu.java.bot.handler.util.HandlerMessages.getLinkRemovedMessage;
import static edu.java.bot.handler.util.HandlerMessages.getNoSuchLinkMessage;
import static edu.java.bot.handler.util.HandlerMessages.getTrackExplanation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UntrackUpdateHandlerTest {

    UntrackUpdateHandler untrackUpdateHandler;

    @Mock
    UserLinksStorageService storageService = Mockito.mock(UserLinksStorageService.class);

    @BeforeEach
    void setup() {
        untrackUpdateHandler = new UntrackUpdateHandler();
        ReflectionTestUtils.setField(untrackUpdateHandler, "linksStorageService", storageService);
    }

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
    void supports_whenHasMessage_shouldSupportOnlyWithListCommand(String text, boolean expected) {
        var update = mockedUpdateWithMessageWithText(text);
        boolean actual = untrackUpdateHandler.supports(update);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void doHandle_whenNotRegistered_shouldReturnNotRegisteredMessage() {
        when(storageService.isRegistered(any())).thenReturn(false);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        assertEqualsSendMessages(actual, createSendMessage(USER_NOT_REGISTERED_YET_MESSAGE));
    }

    @Test
    void doHandle_whenInvalidNumberOfTokens_shouldReturnTrackExplanation() {
        when(storageService.isRegistered(any())).thenReturn(true);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/track link smthg");
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        assertEqualsSendMessages(actual, createSendMessage(getTrackExplanation(Command.UNTRACK.getCommand())));
    }

    @Test
    void doHandle_whenValidTokensButNoSuchLinkTracked_shouldReturnInvalidLinkMessage() {
        when(storageService.isRegistered(any())).thenReturn(true);
        var link = "https://link.not.in.tracked.links.list";
        when(storageService.removeLink(any(), eq(link))).thenReturn(false);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/untrack " + link);
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        verify(storageService).removeLink(any(), eq(link));
        assertEqualsSendMessages(actual, createSendMessage(getNoSuchLinkMessage(link)));
    }

    @Test
    void doHandle_whenLinkInListAndRemoved_shouldReturnLinkNotAddedMessage(){
        when(storageService.isRegistered(any())).thenReturn(true);
        var link = "https://link.com";
        when(storageService.removeLink(any(), eq(link))).thenReturn(true);
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("/untrack " + link);
        SendMessage actual = (SendMessage) untrackUpdateHandler.doHandle(update)
            .orElse(createSendMessage(""));
        verify(storageService).removeLink(any(), eq(link));
        assertEqualsSendMessages(actual, createSendMessage(getLinkRemovedMessage(link)));
    }
}
