package edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;

public class ChatNotRegisteredException extends ApiException {

    public ChatNotRegisteredException() {
        this("Пользователь еще не зарегистрирован.");
    }

    public ChatNotRegisteredException(String description) {
        super(HttpStatus.NOT_FOUND, description);
    }

    public ChatNotRegisteredException(String description, String message) {
        super(HttpStatus.NOT_FOUND, description, message);
    }
}
