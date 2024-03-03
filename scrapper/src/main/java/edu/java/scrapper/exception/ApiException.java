package edu.java.scrapper.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String description;

    public ApiException(HttpStatus status, String description, String message) {
        super(message);
        this.httpStatus = status;
        this.description = description;
    }
}
