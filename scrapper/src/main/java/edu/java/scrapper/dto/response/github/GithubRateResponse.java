package edu.java.scrapper.dto.response.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRateResponse(
    @JsonProperty("rate") GithubRateInfo rateInfo
) {
}
