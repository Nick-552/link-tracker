package edu.java.scrapper.controller;

import edu.java.scrapper.model.Chat;
import edu.java.scrapper.service.tgchats.TgChatsService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TgChatController.class)
class TgChatControllerTest {

    private static final long CHAT_ID = 1;

    private static final String PATH = "/tg-chat/%s".formatted(CHAT_ID);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TgChatsService tgChatsService;

    @Test
    @SneakyThrows
    void registerChat_shouldReturnCorrectOkResponse_whenRequestIsCorrectAndProcessedSuccessfully() {
        Mockito.doNothing().when(tgChatsService).registerChat(new Chat(CHAT_ID));
        mvc.perform(post(PATH))
            .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void deleteChat_shouldReturnCorrectOkResponse_whenRequestIsCorrectAndProcessedSuccessfully() {
        Mockito.doNothing().when(tgChatsService).deleteChat(CHAT_ID);
        mvc.perform(delete(PATH))
            .andExpect(status().isOk());
    }
}
