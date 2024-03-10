package edu.java.bot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdate(
    @JsonProperty @NotNull Long id,
    @JsonProperty @NotNull URI url,
    @JsonProperty @NotNull String description,
    @JsonProperty @NotNull List<Long> tgChatIds
) {
}
