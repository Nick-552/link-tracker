package edu.java.bot.utils;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LinkUtilsTest {

    public static Stream<Arguments> linkSource() {
        return Stream.of(
            Arguments.of("http://", true),
            Arguments.of("https://", true),
            Arguments.of("https://github.com/Nick-552/link-tracker/pull/1", true),
            Arguments.of("fsfghdfghd", false),
            Arguments.of("", false)
        );
    }

    @ParameterizedTest
    @MethodSource("linkSource")
    void isHttpLink(String url, boolean expected) {
        assertThat(LinkUtils.isHttpLink(url)).isEqualTo(expected);
    }

}
