package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkUpdateNotificationService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkUpdatesController.class)
class LinkUpdatesControllerTest {


    @MockBean private LinkUpdateNotificationService linkUpdateNotificationService;

    @Autowired private MockMvc mvc;

    private static final String PATH = "/updates";

    private final LinkUpdate linkUpdate = new LinkUpdate(
        1L,
        URI.create("url"),
        OffsetDateTime.now().toString(),
        "description",
        List.of(1L)
    );

    @Test
    @SneakyThrows
    void sendUpdatedLinks_shouldReturnCorrectOkResponse_whenRequestIsCorrectAndProcessedSuccessfully() {
        Mockito.doNothing().when(linkUpdateNotificationService)
            .notifyAllWithLinkUpdate(linkUpdate);
        mvc.perform(
            post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(linkUpdate))
        ).andExpect(status().isOk());
    }
}
