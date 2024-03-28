package edu.java.bot.utils;

import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MessagesUtilsTest {

    @Test
    void createUpdateMessage() {
        Object chatId = "12345";
        URI link = URI.create("http://example.com");
        OffsetDateTime updatedAt = OffsetDateTime.of(2022, 6, 20, 16, 28, 1, 0, ZoneOffset.ofHours(3));
        String message = "Test message";
        SendMessage result = MessagesUtils.createUpdateMessage(chatId, link, updatedAt, message);
        assertThat(result.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(result.getParameters().get("text")).isEqualTo(
            """
                Обновление ссылки http://example.com
                Обновлена 20.06.2022 в 16:28:01 +03:00
                Подробности:
                Test message
                """
        );
    }
}
