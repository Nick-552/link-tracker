package edu.java.scrapper.service.update;

import java.time.OffsetDateTime;

public record EventInfo(
    OffsetDateTime eventTime,
    String eventDescription
) {
}
