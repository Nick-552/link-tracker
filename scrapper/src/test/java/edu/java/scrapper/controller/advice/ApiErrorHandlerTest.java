package edu.java.scrapper.controller.advice;

import edu.java.scrapper.controller.LinksController;
import edu.java.scrapper.exception.ApiException;
import edu.java.scrapper.service.links.LinksService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinksController.class)
class ApiErrorHandlerTest {

    private static final Long CHAT_ID = 1L;

    private static final String PATH = "/links";

    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LinksService linksService;

    @Test
    @DisplayName("ApiException thrown case")
    @SneakyThrows
    void getLinks_shouldReturnApiErrorResponse_whenApiExceptionThrown() {
        var exception = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "description", "message");
        Mockito.when(linksService.getLinksForChatId(CHAT_ID))
            .thenThrow(exception);
        mvc.perform(
                get(PATH)
                    .header(TG_CHAT_ID_HEADER, CHAT_ID)
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
    void getLinks_shouldReturn400ApiErrorResponse_whenExceptionThrown() {
        Mockito.when(linksService.getLinksForChatId(CHAT_ID))
            .thenThrow(new RuntimeException());
        mvc.perform(
            get(PATH)
                .header(TG_CHAT_ID_HEADER, CHAT_ID)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ResourceNotFoundExceptionThrown thrown case")
    @SneakyThrows
    void getLinks_shouldReturn404ApiErrorResponse_whenResourceNotFoundExceptionThrown() {
        mvc.perform(
            get("/wrong/path")
                .header(TG_CHAT_ID_HEADER, CHAT_ID)
        ).andExpect(status().isNotFound());
    }
}
