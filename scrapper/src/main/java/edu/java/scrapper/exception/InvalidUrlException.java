package edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class InvalidUrlException extends ApiException {

    public InvalidUrlException() {
        this("Неверная ссылка.");
    }

    public InvalidUrlException(String description) {
        super(HttpStatus.BAD_REQUEST, description);
    }

    public InvalidUrlException(String description, String message) {
        super(HttpStatus.BAD_REQUEST, description, message);
    }
}
