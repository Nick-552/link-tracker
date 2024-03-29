package edu.java.scrapper.dto.response.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubRepoInfo(
    @JsonProperty("updated_at") OffsetDateTime lastUpdate,
    @JsonProperty("pushed_at") OffsetDateTime lastPush,
    @JsonProperty("full_name") String name
) {
}
