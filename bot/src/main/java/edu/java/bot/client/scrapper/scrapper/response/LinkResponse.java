package edu.java.bot.client.scrapper.scrapper.response;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
