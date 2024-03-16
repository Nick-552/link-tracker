package edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedUrlException extends ApiException {

    public UnsupportedUrlException() {
        this("Неподдерживаемый вид ссылки.");
    }

    public UnsupportedUrlException(String description) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, description);
    }

    public UnsupportedUrlException(String description, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, description, message);
    }
}
