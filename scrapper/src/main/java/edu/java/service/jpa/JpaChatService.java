package edu.java.service.jpa;

import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.entity.TgChatEntity;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements TelegramChatService {

    private final JpaChatRepository chatRepository;

    private final JpaLinkRepository linkRepository;

    @Override
    @Transactional
    public void registerChat(Long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            throw new ChatAlreadyRegisteredException(tgChatId);
        }
        chatRepository.save(new TgChatEntity(tgChatId));
    }

    @Override
    @Transactional
    public void deleteChat(Long tgChatId) {
        TgChatEntity tgChatEntity =
            chatRepository.findById(tgChatId).orElseThrow(() -> new ChatNotFoundException(tgChatId));

        for (LinkEntity link : tgChatEntity.getLinks()) {
            tgChatEntity.removeLink(link);
            if (link.getTgChats().isEmpty()) {
                linkRepository.delete(link);
            }
        }
        chatRepository.delete(tgChatEntity);
    }
}
