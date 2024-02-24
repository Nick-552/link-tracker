package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.stackoverflow.StackoverflowClient.StackoverflowQuestionInfo;
import edu.java.scrapper.dto.LastLinkUpdate;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest()
class StackoverflowClientTest {

    static StackoverflowClient stackoverflowClient;
    OffsetDateTime lastActivityDateTime = OffsetDateTime
        .ofInstant(
            Instant.ofEpochSecond(1280335676),
            ZoneOffset.UTC
        );

    @BeforeAll
    static void setUpClient(WireMockRuntimeInfo wireMockRuntimeInfo) {
        stackoverflowClient = new StackoverflowClient(
            WebClient.builder(),
            wireMockRuntimeInfo.getHttpBaseUrl()
        );
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
    void getLastUpdate() {
        String url = "https://stackoverflow.com/questions/123/title-of-question";
        var expectedUpdate = new LastLinkUpdate(url, lastActivityDateTime);
        var actualUpdate = stackoverflowClient.getLastUpdate(url);
        assertThat(actualUpdate).isEqualTo(expectedUpdate);
    }

    @Test
    void getStackoverflowQuestionInfo() {
        var expectedInfo = new StackoverflowQuestionInfo(lastActivityDateTime, 7);
        var actualInfo = stackoverflowClient.getStackoverflowQuestionInfo("123");
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }

    @Test
    void getLastUpdate_onStatusError_shouldReturnWithNullLastUpdate() {
        String url = "https://stackoverflow.com/" + "questions/25645634/no-question-on-this-address";
        var expectedUpdate = new LastLinkUpdate(url, null);
        var actualUpdate = stackoverflowClient.getLastUpdate(url);
        assertThat(actualUpdate).isEqualTo(expectedUpdate);
    }

    @Test
    void getStackoverflowQuestionInfo_onStatusError_shouldReturnEmpty() {
        var expectedInfo = StackoverflowQuestionInfo.empty();
        var actualInfo = stackoverflowClient.getStackoverflowQuestionInfo("25645634");
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2656345",
        "https://stackoverflow.com/question/13133695/incompatibleclasschangeerror-with-eclipse-jetty",
        "http://stackoverflow.com/questions/13133695/incompatibleclasschangeerror-with-eclipse-jetty",
        "https://stackoverflow.com/questions/incompatibleclasschangeerror-with-eclipse-jetty",
    })
    @NullAndEmptySource
    void isSupported_whenInvalidUrl_shouldReturnFalse(String url) {
        assertThat(stackoverflowClient.isSupported(url)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://stackoverflow.com/questions/13133695/incompatibleclasschangeerror-with-eclipse-jetty",
        "https://stackoverflow.com/questions/13133695",
        "https://stackoverflow.com/questions/13133695/"
    })
    void isSupported_whenValidUrl_shouldReturnTrue(String url) {
        assertThat(stackoverflowClient.isSupported(url)).isTrue();
    }
}
