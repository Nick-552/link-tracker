package edu.java.scrapper.dto.response.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackoverflowQuestionResponse(
    @JsonProperty("items") StackoverflowQuestionInfo[] items
    ) {
}
