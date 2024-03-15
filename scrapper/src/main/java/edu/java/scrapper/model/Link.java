package edu.java.scrapper.model;

import edu.java.scrapper.dto.response.LinkResponse;
import java.net.URI;
import java.time.OffsetDateTime;

public record Link(Long id, URI url, OffsetDateTime lastUpdatedAt, OffsetDateTime lastCheckAt) {

    public LinkResponse toLinkResponse() {
        return new LinkResponse(id, url);
    }

    public Link getUpdated(OffsetDateTime lastUpdatedAt) {
        return new Link(id, url, lastUpdatedAt, OffsetDateTime.now());
    }
}
