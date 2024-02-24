package edu.java.scrapper.client;

import edu.java.scrapper.dto.LastLinkUpdate;

public interface LinkUpdatesClient {

    LastLinkUpdate getLastUpdate(String url);
}
