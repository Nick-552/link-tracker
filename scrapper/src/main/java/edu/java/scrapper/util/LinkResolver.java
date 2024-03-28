package edu.java.scrapper.util;

import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.model.LinkType;
import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkResolver {

    private static final Map<Pattern, LinkType> LINK_TYPES = Map.of(
        Pattern.compile("^https://github.com/[\\w-]+/[\\w-]+/?$"), LinkType.GITHUB_REPO,
        Pattern.compile("^https://stackoverflow.com/questions/[\\d]+(/[\\w-]*)?$"), LinkType.STACKOVERFLOW_QUESTION
    );

    public static LinkType getLinkType(URI url) {
        for (var pattern: LINK_TYPES.keySet()) {
            if (pattern.matcher(url.toString()).matches()) {
                return LINK_TYPES.get(pattern);
            }
        }
        throw new UnsupportedUrlException();
    }
}
