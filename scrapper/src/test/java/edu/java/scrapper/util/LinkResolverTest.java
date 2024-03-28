package edu.java.scrapper.util;

import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.model.LinkType;
import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LinkResolverTest {

    public static Stream<Arguments> validLinksSource() {
        return Stream.of(
            Arguments.of("https://github.com/Nick-552/link-tracker/", LinkType.GITHUB_REPO),
            Arguments.of("https://stackoverflow.com/questions/1234567/fghdfghdfghdf", LinkType.STACKOVERFLOW_QUESTION),
            Arguments.of("https://github.com/Nick-552/link-tracker", LinkType.GITHUB_REPO),
            Arguments.of("https://stackoverflow.com/questions/1234567", LinkType.STACKOVERFLOW_QUESTION),
            Arguments.of("https://stackoverflow.com/questions/1234567/", LinkType.STACKOVERFLOW_QUESTION)
            );
    }

    public static Stream<Arguments> invalidLinksSource() {
        return Stream.of(
            Arguments.of("https://github.com/"),
            Arguments.of("https://stackoverflow.com/"),
            Arguments.of("https://stackoverflow.com/questions/gfhdfgh/")
        );
    }

    @ParameterizedTest
    @MethodSource("validLinksSource")
    void getLinkType_whenValid_returns(String link, LinkType expected) {
        var uri = URI.create(link);
        assertThat(LinkResolver.getLinkType(uri)).isEqualTo(expected);
    }

    @ParameterizedTest
    @EmptySource
    void getLinkType_whenNullOrEmpty_shouldThrow(String link) {
        var uri = URI.create(link);
        assertThatExceptionOfType(UnsupportedUrlException.class).isThrownBy(() -> LinkResolver.getLinkType(uri));
    }

    @ParameterizedTest
    @MethodSource("invalidLinksSource")
    void getLinkType_whenInvalid_shouldThrow(String link) {
        var uri = URI.create(link);
        assertThatExceptionOfType(UnsupportedUrlException.class).isThrownBy(() -> LinkResolver.getLinkType(uri));
    }

}
