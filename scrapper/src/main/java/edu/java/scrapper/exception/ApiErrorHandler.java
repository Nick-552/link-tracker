package edu.java.scrapper.exception;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import java.util.Arrays;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandler {

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
}
