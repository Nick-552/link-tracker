package edu.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackoverflowQuestionInfo(
    @JsonProperty("last_activity_date") OffsetDateTime lastUpdate,
    @JsonProperty("answer_count") Integer answerCount
) {
}
