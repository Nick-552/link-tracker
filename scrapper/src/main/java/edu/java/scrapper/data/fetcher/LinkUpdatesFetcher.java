package edu.java.scrapper.data.fetcher;

import edu.java.scrapper.dto.LastLinkUpdate;

public interface LinkUpdatesFetcher {

    LastLinkUpdate getLastUpdate(String url);
}
