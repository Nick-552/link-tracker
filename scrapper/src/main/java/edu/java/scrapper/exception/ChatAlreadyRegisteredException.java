package edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends ApiException {

    public ChatAlreadyRegisteredException() {
        this("Пользователь уже зарегистрирован.");
    }

    public ChatAlreadyRegisteredException(String description) {
        super(HttpStatus.CONFLICT, description);
    }

    public ChatAlreadyRegisteredException(String description, String message) {
        super(HttpStatus.CONFLICT, description, message);
    }
}
