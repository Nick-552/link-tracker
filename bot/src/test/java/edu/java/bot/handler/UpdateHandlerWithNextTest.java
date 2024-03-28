package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import static edu.java.bot.handler.handlers.HandlerTestUtils.DEFAULT_CHAT_ID;
import static edu.java.bot.handler.handlers.HandlerTestUtils.assertEqualsSendMessages;
import static edu.java.bot.handler.util.HandlerUtils.defaultHandle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateHandlerWithNextTest {

    UpdateHandlerWithNext updateHandlerWithNext =  Mockito.mock(UpdateHandlerWithNext.class, Answers.CALLS_REAL_METHODS);
    UpdateHandler next = Mockito.mock(UpdateHandler.class);
    SendMessage sm = new SendMessage(DEFAULT_CHAT_ID, "message");
    SendMessage smNext = new SendMessage(DEFAULT_CHAT_ID, "messageNext");

    @BeforeEach
    void setNextHandlerReturn() {
        Mockito.doReturn(smNext).when(next).handle(any());
    }

    @Test
    void setNextHandler() {
        var expectedThatNext = updateHandlerWithNext.setNextHandler(next);
        assertThat(updateHandlerWithNext.nextHandler).isEqualTo(next);
        assertThat(expectedThatNext).isEqualTo(next);
    }

    @Test
    void testSetNextHandler() {
        var expectedThatNext = updateHandlerWithNext.setNextHandler(next);
        assertThat(updateHandlerWithNext.nextHandler).isEqualTo(next);
        assertThat(expectedThatNext).isEqualTo(next);
    }

    @Test
    void nextHandle_whenNextIsNull() {
        updateHandlerWithNext.setNextHandler(null);
        var result = updateHandlerWithNext.nextHandle(new Update());
        assertEqualsSendMessages((SendMessage) result, (SendMessage) defaultHandle(new Update()));
    }

    @Test
    void nextHandle_whenNextIsntNull() {
        updateHandlerWithNext.setNextHandler(next);
        var result = updateHandlerWithNext.nextHandle(new Update());
        assertEqualsSendMessages((SendMessage) result, smNext);
    }

    @Test
    void handle_whenSupportsAndDoHandleEmpty() {
        updateHandlerWithNext.setNextHandler(next);

        when(updateHandlerWithNext.supports(any())).thenReturn(true);
        when(updateHandlerWithNext.doHandle(any())).thenReturn(Optional.empty());

        var actual = updateHandlerWithNext.handle(new Update());

        verify(updateHandlerWithNext).doHandle(any());
        verify(updateHandlerWithNext).nextHandle(any());
        assertEqualsSendMessages((SendMessage) actual, smNext);
    }

    @Test
    void handle_whenDoesntSupportAndDoHandleEmpty() {
        updateHandlerWithNext.setNextHandler(next);

        when(updateHandlerWithNext.supports(any())).thenReturn(false);
        when(updateHandlerWithNext.doHandle(any())).thenReturn(Optional.empty());

        var actual = updateHandlerWithNext.handle(new Update());

        verify(updateHandlerWithNext, atMost(0)).doHandle(any());
        verify(updateHandlerWithNext).nextHandle(any());
        assertEqualsSendMessages((SendMessage) actual, smNext);
    }

    @Test
    void handle_whenSupportsAndDoHandleNotEmpty() {
        updateHandlerWithNext.setNextHandler(next);

        when(updateHandlerWithNext.supports(any())).thenReturn(true);
        when(updateHandlerWithNext.doHandle(any())).thenReturn(Optional.of(sm));

        var actual = updateHandlerWithNext.handle(new Update());

        verify(updateHandlerWithNext).doHandle(any());
        verify(updateHandlerWithNext, atMost(0)).nextHandle(any());
        assertEqualsSendMessages((SendMessage) actual, sm);
    }

    @Test
    void handle_whenDoesntSupportAndDoHandleNotEmpty() {
        updateHandlerWithNext.setNextHandler(next);

        when(updateHandlerWithNext.supports(any())).thenReturn(false);
        when(updateHandlerWithNext.doHandle(any())).thenReturn(Optional.of(sm));

        var actual = updateHandlerWithNext.handle(new Update());

        verify(updateHandlerWithNext, atMost(0)).doHandle(any());
        verify(updateHandlerWithNext).nextHandle(any());
        assertEqualsSendMessages((SendMessage) actual, smNext);
    }
}
