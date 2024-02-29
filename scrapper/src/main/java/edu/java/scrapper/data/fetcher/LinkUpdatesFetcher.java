package edu.java.scrapper.data.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.scrapper.dto.LastLinkUpdate;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface LinkUpdatesFetcher {

    LastLinkUpdate getLastUpdate(String url) throws JSONException, JsonProcessingException;
}
