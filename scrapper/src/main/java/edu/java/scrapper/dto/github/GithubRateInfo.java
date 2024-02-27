package edu.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubRateInfo(
    @JsonProperty("limit") Integer limit,
    @JsonProperty("used") Integer used,
    @JsonProperty("remaining") Integer remaining,
    @JsonProperty("reset") OffsetDateTime reset
) {
}
