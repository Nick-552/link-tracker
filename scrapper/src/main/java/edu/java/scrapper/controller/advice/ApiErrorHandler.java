package edu.java.scrapper.controller.advice;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.ApiException;
import java.util.Arrays;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Log4j2
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
        log.info(e.getClass());
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
                status.toString(),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            )
        );
    }
}
