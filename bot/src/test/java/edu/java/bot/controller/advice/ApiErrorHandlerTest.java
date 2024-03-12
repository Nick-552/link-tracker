package edu.java.bot.controller.advice;

import edu.java.bot.controller.LinkUpdatesController;
import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.exception.ApiException;
import edu.java.bot.service.LinkUpdateNotificationService;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkUpdatesController.class)
class ApiErrorHandlerTest {

    private static final Long CHAT_ID = 1L;

    private static final String PATH = "/updates";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LinkUpdateNotificationService notificationService;

    private static final LinkUpdate LINK_UPDATE = new LinkUpdate(
        1L,
        URI.create("url"),
        "description",
        List.of(CHAT_ID)
    );

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("ApiException thrown case")
    @SneakyThrows
    void sendUpdatedLink_shouldReturnApiErrorResponse_whenApiExceptionThrown() {
        var exception = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "description", "message");
        Mockito.doThrow(exception).when(notificationService)
            .notifyAllWithLinkUpdate(LINK_UPDATE);
        mvc.perform(
            post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(LINK_UPDATE))
            ).andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.description").value("description"))
            .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
            .andExpect(jsonPath("$.exceptionName").exists())
            .andExpect(jsonPath("$.exceptionMessage").value("message"))
            .andExpect(jsonPath("$.stacktrace").isArray());
    }

    @Test
    @DisplayName("Exception thrown case")
    @SneakyThrows
    void sendUpdatedLink_shouldReturn400ApiErrorResponse_whenExceptionThrown() {
        // no body and no media type header
        mvc.perform(post(PATH))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ResourceNotFoundExceptionThrown thrown case")
    @SneakyThrows
    void sendUpdatedLink_shouldReturn404ApiErrorResponse_whenResourceNotFoundExceptionThrown() {
        mvc.perform(
            post("/wrong/path")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(LINK_UPDATE))
            ).andExpect(status().isNotFound());
    }
}
