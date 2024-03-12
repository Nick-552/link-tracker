package edu.java.bot.dto.response.scrapper;

import java.util.List;

public record LinksListResponse(
    List<LinkResponse> links,
    Integer size
) {
}
