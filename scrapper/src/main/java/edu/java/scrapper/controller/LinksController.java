package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.service.LinksService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/links", consumes = "application/json", produces = "application/json")
public class LinksController {

    private final LinksService linksService;

    public LinksController(LinksService linksService) {
        this.linksService = linksService;
    }

    @GetMapping
    public LinksListResponse getLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        return linksService.getLinks(chatId);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        return linksService.addLink(chatId, addLinkRequest);
    }

    public @DeleteMapping
    LinkResponse removeLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        return linksService.removeLink(chatId, removeLinkRequest);
    }
}
