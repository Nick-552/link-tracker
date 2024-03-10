package edu.java.bot.controller.advice;

import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exception.ApiException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException e) {
        return createApiErrorResponseEntity(
            e,
            e.getHttpStatus(),
            e.getDescription()
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        return createApiErrorResponseEntity(
            e,
            e.getStatusCode(),
            e.getBody().getDetail()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        return createApiErrorResponseEntity(
            e,
            HttpStatus.BAD_REQUEST,
            "Некорректные параметры запроса"
        );
    }

    private ResponseEntity<ApiErrorResponse> createApiErrorResponseEntity(
        Exception e,
        HttpStatusCode status,
        String description
    ) {
        return ResponseEntity.status(status).body(
            new ApiErrorResponse(
                description,
                HttpStatus.valueOf(status.value()),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            )
        );
    }
}
