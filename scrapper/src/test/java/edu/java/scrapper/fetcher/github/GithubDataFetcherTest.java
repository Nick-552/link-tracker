package edu.java.scrapper.fetcher.github;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.github.GithubClient;
import edu.java.scrapper.dto.response.github.GithubRateInfo;
import edu.java.scrapper.dto.response.github.GithubRepoInfo;
import edu.java.scrapper.exception.InvalidUrlException;
import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.service.fetcher.GithubDataFetcher;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.SneakyThrows;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@WireMockTest
class GithubDataFetcherTest {

    static GithubDataFetcher githubDataFetcher;
    String lastActivityDateTimeString = "2024-02-02T13:06:20Z";
    int resetInt = 1708986679;
    String owner = "nick";
    String name = "tracker";
    String fullName = "/" + owner + "/" + name;
    String host = "https://github.com";
    OffsetDateTime lastActivityDateTime = OffsetDateTime.parse(lastActivityDateTimeString);
    OffsetDateTime reset = OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(resetInt),
            ZoneOffset.UTC
        );


    @BeforeAll
    static void setUpClient(WireMockRuntimeInfo wireMockRuntimeInfo) {
        var githubClient = new GithubClient(
            WebClient.builder(),
            wireMockRuntimeInfo.getHttpBaseUrl()
        );
        githubDataFetcher = new GithubDataFetcher(githubClient);
    }

    @BeforeEach
    void setUpStubs() {
        stubFor(get("/repos" + fullName)
            .willReturn(okJson("""
                {
                    "some_properties": "asfas",
                    "full_name": "%s",
                    "other": "sfdgsdf",
                    "updated_at": "%s",
                    "pushed_at": "%s",
                    "and_more": "fgsdfgsdfg",
                    "and_list": [12,2131,14,2134]
                }
                """.formatted(fullName, lastActivityDateTimeString, lastActivityDateTimeString)
            )));
        stubFor(get("/rate_limit")
            .willReturn(okJson("""
                {
                    "rate": {
                        "limit": "5000",
                        "used": "120",
                        "remaining": "4880",
                        "reset": "1708986679"
                    }
                }
                """
            )));
    }


    @Test
    @SneakyThrows
    void getGithubRepoInfo_whenOk_shouldReturnExpectedInfo() {
        var expectedInfo = new GithubRepoInfo(lastActivityDateTime, lastActivityDateTime, fullName);
        var actualInfo = githubDataFetcher.getRepoInfo(URI.create(host + fullName));
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }

    @Test
    @SneakyThrows
    void getLastUpdate_onStatusError_shouldThrowWebClientResponseException() {
        String stringUrl = host + "/wrong-address/will-be-not-found-or-smth";
        AssertionsForClassTypes.assertThatExceptionOfType(InvalidUrlException.class)
            .isThrownBy(() -> githubDataFetcher.getRepoInfo(URI.create(stringUrl)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2656345",
        "https://github.com/owner/repo-name/smthg",
        "http://github.com/owner/repo-name"
    })
    @EmptySource
    void getLastUpdate_whenInvalidUrl_shouldThrowUnsupportedUrlException(String url) {
        assertThatExceptionOfType(UnsupportedUrlException.class)
            .isThrownBy(() -> githubDataFetcher.getRepoInfo(URI.create(url)));
    }

    @Test
    @SneakyThrows
    void getRateInfo() {
        var expectedInfo = new GithubRateInfo(5000, 120, 4880, reset);
        var actualInfo = githubDataFetcher.getRateInfo();
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }
}
