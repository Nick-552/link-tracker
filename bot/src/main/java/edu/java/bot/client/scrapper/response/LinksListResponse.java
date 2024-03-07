package edu.java.bot.client.scrapper.response;

import java.util.List;

public record LinksListResponse(
    List<LinkResponse> links,
    Integer size
) {
}
