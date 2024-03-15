package edu.java.scrapper.service.fetcher;

import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.dto.response.stackoverflow.StackoverflowQuestionInfo;
import edu.java.scrapper.exception.UnsupportedUrlException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackoverflowDataFetcher {

    private static final Pattern QUESTION_PATTERN = Pattern
        .compile("^https://stackoverflow.com/questions/(\\d+)(/[\\w-]*)?$");

    private final StackoverflowClient stackoverflowClient;

    public StackoverflowQuestionInfo getQuestionInfo(URI url) {
        Matcher matcher = QUESTION_PATTERN.matcher(url.toString());
        if (matcher.matches()) {
            String questionId = matcher.group(1);
            return stackoverflowClient.getQuestionResponse(questionId).items()[0];
        }
        throw new UnsupportedUrlException();
    }
}
