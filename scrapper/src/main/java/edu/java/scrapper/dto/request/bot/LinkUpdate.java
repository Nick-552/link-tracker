package edu.java.scrapper.dto.request.bot;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public record LinkUpdate(
    Long id,
    URI url,
    OffsetDateTime updatedAt,
    String description,
    List<Long> tgChatIds
) {
}
