package edu.java.scrapper.dto.request.bot;

import java.net.URI;
import java.util.List;

public record LinkUpdate(
    Long id,
    URI url,
    String updatedAt,
    String description,
    List<Long> tgChatIds
) {
}
