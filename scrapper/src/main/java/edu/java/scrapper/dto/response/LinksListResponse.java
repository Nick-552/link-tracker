package edu.java.scrapper.dto.response;

import java.util.List;

public record LinksListResponse(
    List<LinkResponse> links,
    Integer size
) {
}
