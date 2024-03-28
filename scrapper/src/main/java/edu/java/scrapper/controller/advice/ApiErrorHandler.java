package edu.java.scrapper.controller.advice;

import edu.java.scrapper.dto.response.ApiErrorResponse;
import edu.java.scrapper.exception.ApiException;
import java.util.Arrays;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
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

    @ExceptionHandler({
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class,
        MissingServletRequestPartException.class,
        ServletRequestBindingException.class,
        MethodArgumentNotValidException.class,
        HandlerMethodValidationException.class,
        NoHandlerFoundException.class,
        NoResourceFoundException.class,
        AsyncRequestTimeoutException.class,
        ErrorResponseException.class,
        MaxUploadSizeExceededException.class
    })
    public ResponseEntity<ApiErrorResponse> handleControllerException(ErrorResponse e) {
        return createApiErrorResponseEntity(
            (Exception) e,
            e.getStatusCode(),
            e.getBody().getDetail()
        );
    }

    @ExceptionHandler({
        ConversionNotSupportedException.class,
        TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        HttpMessageNotWritableException.class,
        MethodValidationException.class,
        BindException.class
    })
    public ResponseEntity<ApiErrorResponse> handleControllerException(Exception e) {
        return createApiErrorResponseEntity(
            e,
            HttpStatus.BAD_REQUEST,
            "Некорректные данные запроса"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        return createApiErrorResponseEntity(
            e,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Неизвестная ошибка сервера"
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
