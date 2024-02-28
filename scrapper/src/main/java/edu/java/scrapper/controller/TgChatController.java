package edu.java.scrapper.controller;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.service.TgChatsService;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tg-chat/{id}", consumes = "application/json", produces = "application/json")
public class TgChatController {

    private final TgChatsService tgChatsService;

    public TgChatController(TgChatsService tgChatsService) {
        this.tgChatsService = tgChatsService;
    }

    @PostMapping
    @Schema(implementation = ApiErrorResponse.class)
    public void registerChat(@PathVariable Long id) {
        tgChatsService.registerChat(id);
    }

    @DeleteMapping
    public void deleteChat(@PathVariable Long id) {
        tgChatsService.deleteChat(id);
    }
}
