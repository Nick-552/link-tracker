package edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class LinkAlreadyTrackedException extends ApiException {

    public LinkAlreadyTrackedException() {
        this("Ссылка уже отслеживается.");
    }

    public LinkAlreadyTrackedException(String description) {
        super(HttpStatus.CONFLICT, description);
    }

    public LinkAlreadyTrackedException(String description, String message) {
        super(HttpStatus.CONFLICT, description, message);
    }
}
