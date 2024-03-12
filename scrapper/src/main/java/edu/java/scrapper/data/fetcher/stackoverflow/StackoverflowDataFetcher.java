package edu.java.scrapper.data.fetcher.stackoverflow;

import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.data.fetcher.AbstractDataFetcher;
import edu.java.scrapper.data.fetcher.LinkUpdatesFetcher;
import edu.java.scrapper.dto.LastLinkUpdate;
import edu.java.scrapper.dto.response.stackoverflow.StackoverflowQuestionInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackoverflowDataFetcher extends AbstractDataFetcher implements LinkUpdatesFetcher {

    private static final Pattern QUESTION_PATTERN = Pattern
        .compile("^https://stackoverflow.com/questions/(\\d+)(/[\\w-]*)?$");

    private final StackoverflowClient stackoverflowClient;

    @Override
    public LastLinkUpdate getLastUpdate(String url) {
        Matcher matcher = QUESTION_PATTERN.matcher(url);
        if (matcher.matches()) {
            String questionId = matcher.group(1);
            return new LastLinkUpdate(url, getQuestionInfo(questionId).lastUpdate());
        }
        throw new UnsupportedUrlException();
    }

    public StackoverflowQuestionInfo getQuestionInfo(String questionId) {
        return stackoverflowClient.getQuestionResponse(questionId).items()[0];
    }
}
