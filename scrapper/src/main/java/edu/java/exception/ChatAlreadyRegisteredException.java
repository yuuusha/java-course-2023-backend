package edu.java.exception;

import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends ScrapperException {
    public ChatAlreadyRegisteredException(Long chatId) {
        super(
            "Chat already registered",
            String.format("Chat %d is already registered", chatId),
            HttpStatus.BAD_REQUEST
        );
    }
}
