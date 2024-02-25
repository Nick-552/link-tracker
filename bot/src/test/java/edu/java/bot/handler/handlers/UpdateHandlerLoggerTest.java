package edu.java.bot.handler.handlers;

import com.pengrad.telegrambot.model.Update;
import java.util.Optional;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateHandlerLoggerTest {

    @Mock
    private Logger mocked = mock(Logger.class);

    private final UpdateHandlerLogger updateHandlerLogger = new UpdateHandlerLogger(mocked);

    @Test
    void doHandle_always_shouldLog() {
        Update update = HandlerTestUtils.mockedUpdateWithMessageWithText("no matter what");
        var actual = updateHandlerLogger.doHandle(update);
        verify(mocked).info(update.toString());
        assertThat(actual).isEqualTo(Optional.empty());
    }
}
