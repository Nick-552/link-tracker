package edu.java.scrapper.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
@ToString
public class ApiException extends RuntimeException {

    private final HttpStatusCode httpStatus;

    private final String description;

    public ApiException(HttpStatusCode status, String description, String message) {
        super(message);
        this.httpStatus = status;
        this.description = description;
    }
}
