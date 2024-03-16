package edu.java.scrapper.data.fetcher;

public abstract class AbstractDataFetcher {

    public static class UnsupportedUrlException extends IllegalArgumentException {

        private static final String DEFAULT_MESSAGE = "This url isn't supported by method";

        public UnsupportedUrlException() {
            super(DEFAULT_MESSAGE);
        }
    }
}
