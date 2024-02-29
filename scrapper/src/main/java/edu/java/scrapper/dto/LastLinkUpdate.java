package edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record LastLinkUpdate(String url, OffsetDateTime lastUpdateTime) {
}
