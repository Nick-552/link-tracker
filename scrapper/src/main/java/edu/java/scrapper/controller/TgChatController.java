package edu.java.scrapper.controller;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.service.tgchats.TgChatsService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/tg-chat/{id}", produces = "application/json")
public class TgChatController {

    private final TgChatsService tgChatsService;

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
