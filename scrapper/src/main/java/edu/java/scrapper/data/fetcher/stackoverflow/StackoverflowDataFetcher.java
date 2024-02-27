package edu.java.scrapper.data.fetcher.stackoverflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.data.fetcher.AbstractDataFetcher;
import edu.java.scrapper.data.fetcher.LinkUpdatesFetcher;
import edu.java.scrapper.dto.LastLinkUpdate;
import edu.java.scrapper.dto.stackoverflow.StackoverflowQuestionInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StackoverflowDataFetcher extends AbstractDataFetcher implements LinkUpdatesFetcher {

    private static final String QUESTION_URI = "/questions/%s?site=stackoverflow";

    private static final Pattern QUESTION_PATTERN = Pattern
        .compile("^https://stackoverflow.com/questions/(\\d+)(/[\\w-]*)?$");

    private final StackoverflowClient stackoverflowClient;

    public StackoverflowDataFetcher(StackoverflowClient client) {
        this.stackoverflowClient = client;
    }

    @Override
    public LastLinkUpdate getLastUpdate(String url) throws JSONException, JsonProcessingException {
        Matcher matcher = QUESTION_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new UnsupportedUrlException();
        }
        String questionId = matcher.group(1);
        return new LastLinkUpdate(url, getQuestionInfo(questionId).lastUpdate());
    }

    public StackoverflowQuestionInfo getQuestionInfo(String questionId)
        throws JSONException, JsonProcessingException {
        var questionJson = stackoverflowClient.getJson(QUESTION_URI.formatted(questionId))
            .getJSONArray("items").getJSONObject(0);
        return objectMapper.readValue(questionJson.toString(), StackoverflowQuestionInfo.class);
    }
}
