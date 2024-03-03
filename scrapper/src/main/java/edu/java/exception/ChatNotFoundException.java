package edu.java.exception;

import org.springframework.http.HttpStatus;

public class ChatNotFoundException extends ScrapperException {
    public ChatNotFoundException(Long chatId) {
        super("Chat not found", String.format("Chat %d not found", chatId), HttpStatus.NOT_FOUND);
    }
}
