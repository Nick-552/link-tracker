package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.github.GithubClient.GithubRepoInfo;
import edu.java.scrapper.dto.LastLinkUpdate;
import java.time.OffsetDateTime;
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

@WireMockTest
class GithubClientTest {

    static GithubClient githubClient;
    String lastActivityDateTimeString = "2024-02-02T13:06:20Z";
    String owner = "nick";
    String name = "tracker";
    String fullName = owner + "/" + name;
    OffsetDateTime lastActivityDateTime = OffsetDateTime.parse(lastActivityDateTimeString);


    @BeforeAll
    static void setUpClient(WireMockRuntimeInfo wireMockRuntimeInfo) {
        githubClient = new GithubClient(
            WebClient.builder(),
            wireMockRuntimeInfo.getHttpBaseUrl()
        );
    }

    @BeforeEach
    void setUpStub() {
        stubFor(get("/repos/" + fullName)
            .willReturn(okJson("""
                {
                    "some_properties": "asfas",
                    "full_name": "%s",
                    "other": "sfdgsdf",
                    "updated_at": "%s",
                    "and_more": "fgsdfgsdfg",
                    "and_list": [12,2131,14,2134]
                }
                """.formatted(fullName, lastActivityDateTimeString)
            )));
    }

    @Test
    void getLastUpdate() {
        String url = "https://github.com/" + fullName;
        var expectedUpdate = new LastLinkUpdate(url, lastActivityDateTime);
        var actualUpdate = githubClient.getLastUpdate(url);
        assertThat(actualUpdate).isEqualTo(expectedUpdate);
    }

    @Test
    void getGithubRepoInfo() {
        var expectedInfo = new GithubRepoInfo(lastActivityDateTime, fullName);
        var actualInfo = githubClient.getGithubRepoInfo(owner, name);
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }

    @Test
    void getLastUpdate_onStatusError_shouldReturnWithNullLastUpdate() {
        String url = "https://github.com/" + "wrong-address/will-be-not-found-or-smth";
        var expectedUpdate = new LastLinkUpdate(url, null);
        var actualUpdate = githubClient.getLastUpdate(url);
        assertThat(actualUpdate).isEqualTo(expectedUpdate);
    }

    @Test
    void getGithubRepoInfo_onStatusError_shouldReturnEmpty() {
        var expectedInfo = GithubRepoInfo.empty();
        var actualInfo = githubClient.getGithubRepoInfo("wrong", "address");
        assertThat(actualInfo).isEqualTo(expectedInfo);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2656345",
        "https://github.com/owner/repo-name/smthg",
        "http://github.com/owner/repo-name"
    })
    @NullAndEmptySource
    void isSupported_whenInvalidUrl_shouldReturnFalse(String url) {
        assertThat(githubClient.isSupported(url)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://github.com/owner/repo-name",
        "https://github.com/owner/repo/"
    })
    void isSupported_whenValidUrl_shouldReturnTrue(String url) {
        assertThat(githubClient.isSupported(url)).isTrue();
    }
}
