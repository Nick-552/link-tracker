package edu.java.bot.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkUtils {

    private static final String HTTP_LINK_START = "http://";

    private static final String HTTPS_LINK_START = "https://";

    public static boolean isHttpLink(String url) {
        return url.startsWith(HTTPS_LINK_START) || url.startsWith(HTTP_LINK_START);
    }
}
