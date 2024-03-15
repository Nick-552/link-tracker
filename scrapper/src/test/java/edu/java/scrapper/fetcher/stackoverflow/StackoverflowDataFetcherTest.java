package edu.java.scrapper.fetcher.stackoverflow;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient;
import edu.java.scrapper.dto.response.stackoverflow.StackoverflowQuestionInfo;
import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.service.fetcher.StackoverflowDataFetcher;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@WireMockTest
class StackoverflowDataFetcherTest {

    String host = "https://stackoverflow.com";
    String questionUrl = host + "/questions";

    static StackoverflowDataFetcher stackoverflowDataFetcher;
    OffsetDateTime lastActivityDateTime = OffsetDateTime
        .ofInstant(
            Instant.ofEpochSecond(1280335676),
            ZoneOffset.UTC
        );

    @BeforeAll
    static void setUpClient(WireMockRuntimeInfo wireMockRuntimeInfo) {
        var stackoverflowClient = new StackoverflowClient(
            WebClient.builder(),
            wireMockRuntimeInfo.getHttpBaseUrl()
        );
        stackoverflowDataFetcher = new StackoverflowDataFetcher(stackoverflowClient);
    }

    @BeforeEach
    void setUpStub() {
        stubFor(get("/questions/123?site=stackoverflow")
            .willReturn(okJson("""
                {
                    "items":[
                        {
                            "answer_count":7,
                            "last_activity_date":1280335676,
                            "title":"Validating arguments to a method"
                        }
                    ],
                    "just_one_else":"value"
                }
                """
            )));
    }

    @Test
    @SneakyThrows
    void getQuestionInfo_whenOk() {
        var expectedInfo = new StackoverflowQuestionInfo(lastActivityDateTime, 7);
        var actualInfo = stackoverflowDataFetcher.getQuestionInfo(URI.create(questionUrl + "/123"));
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }


    @Test
    @SneakyThrows
    void getQuestionInfo_onStatusError_shouldThrowWebClientResponseException() {
        assertThatExceptionOfType(WebClientResponseException.class)
            .isThrownBy(() -> stackoverflowDataFetcher.getQuestionInfo(URI.create(questionUrl + "/25645634/no-stub-there")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2656345",
        "https://stackoverflow.com/question/13133695/incompatibleclasschangeerror-with-eclipse-jetty",
        "http://stackoverflow.com/questions/13133695/incompatibleclasschangeerror-with-eclipse-jetty",
        "https://stackoverflow.com/questions/incompatibleclasschangeerror-with-eclipse-jetty",
    })
    @EmptySource
    void getQuestionInfo_whenInvalidUrl_shouldThrowUnsupportedUrlException(String url) {
        assertThatExceptionOfType(UnsupportedUrlException.class)
            .isThrownBy(() -> stackoverflowDataFetcher.getQuestionInfo(URI.create(url)));
    }
}
