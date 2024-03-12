package edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends ApiException {

    public LinkNotFoundException() {
        this("Ссылка не найдена.");
    }

    public LinkNotFoundException(String description) {
        super(HttpStatus.NOT_FOUND, description);
    }

    public LinkNotFoundException(String description, String message) {
        super(HttpStatus.NOT_FOUND, description, message);
    }
}
