package edu.java.bot.dto.response.scrapper;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
