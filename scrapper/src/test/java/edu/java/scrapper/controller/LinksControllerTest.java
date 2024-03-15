package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.service.links.LinksService;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinksController.class)
@Log4j2
class LinksControllerTest {

    private static final Long CHAT_ID = 1L;

    private static final String PATH = "/links";

    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LinksService linksService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @SneakyThrows
    void getLinks_shouldReturnCorrectOkResponse_whenRequestIsCorrectAndProcessedSuccessfully() {
        Mockito.when(linksService.getLinksForChatId(CHAT_ID)).thenReturn(
            new LinksListResponse(
                List.of(
                    new LinkResponse(CHAT_ID, URI.create("url"))
                ), 1
            )
        );
        mvc.perform(get(PATH)
                .header(TG_CHAT_ID_HEADER, CHAT_ID)
            ).andExpect(status().isOk())
            .andExpect(content().json(
                "{\"links\":[{\"id\":1,\"url\":\"url\"}],\"size\":1}"
            ));
    }

    @Test
    @SneakyThrows
    void addLink_shouldReturnCorrectOkResponse_whenRequestIsCorrectAndProcessedSuccessfully() {
        var link = "link";
        var request = new AddLinkRequest(URI.create(link));
        Mockito.when(linksService.addLinkToChat(CHAT_ID, request))
            .thenReturn(new LinkResponse(1L, URI.create(link)));
        mvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TG_CHAT_ID_HEADER, CHAT_ID)
                .content(mapper.writeValueAsString(request))
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.url").value(link));
    }

    @Test
    @SneakyThrows
    void removeLink_shouldReturnCorrectOkResponse_whenRequestIsCorrectAndProcessedSuccessfully() {
        var link = "link";
        var request = new RemoveLinkRequest(URI.create(link));
        Mockito.when(linksService.removeLinkFromChat(CHAT_ID, request))
            .thenReturn(new LinkResponse(1L, URI.create(link)));
        mvc.perform(delete(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TG_CHAT_ID_HEADER, CHAT_ID)
                .content(mapper.writeValueAsString(request))
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.url").value(link));
    }
}
