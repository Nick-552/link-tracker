package edu.java.bot.controller;

import edu.java.bot.dto.request.LinkUpdate;
import edu.java.bot.service.LinkUpdateNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/updates", consumes = "application/json", produces = "application/json")
public class LinkUpdatesController {

    private final LinkUpdateNotificationService notificationService;

    @PostMapping
    public void sendUpdatedLinks(@RequestBody @Valid LinkUpdate linkUpdate) {
        notificationService.notifyAllWithLinkUpdate(linkUpdate);
    }
}
