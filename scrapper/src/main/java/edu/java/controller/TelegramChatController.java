package edu.java.controller;

import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat/{id}")
@RequiredArgsConstructor
public class TelegramChatController {

    private final TelegramChatService service;

    @PostMapping
    public void registerChat(@PathVariable Long id) {
        service.registerChat(id);
    }

    @DeleteMapping
    public void deleteChat(@PathVariable Long id) {
        service.deleteChat(id);
    }
}
