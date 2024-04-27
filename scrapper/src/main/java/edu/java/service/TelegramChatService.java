package edu.java.service;


public interface TelegramChatService {

    void registerChat(Long chatId);

    void deleteChat(Long chatId);
}
