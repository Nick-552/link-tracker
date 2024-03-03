package edu.java.scrapper.data.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class AbstractDataFetcher {

    protected final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static class UnsupportedUrlException extends IllegalArgumentException {

        private static final String DEFAULT_MESSAGE = "This url isn't supported by method";

        public UnsupportedUrlException() {
            super(DEFAULT_MESSAGE);
        }
    }
}
