package edu.java.scrapper.controller.advice;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.ApiException;
import java.util.Arrays;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(
            new ApiErrorResponse(
                e.getDescription(),
                e.getHttpStatus().toString(),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            )
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ApiErrorResponse> handleApiErrorException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ApiErrorResponse(
                "Ссылка не найдена",
                HttpStatus.NOT_FOUND.toString(),
                ex.getClass().getName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ApiErrorResponse(
                "Некорректные параметры запроса",
                HttpStatus.BAD_REQUEST.toString(),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            )
        );
    }
}
