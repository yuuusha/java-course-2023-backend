package edu.java.service.jdbc;

import edu.java.dto.Link;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.service.TelegramChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcTelegramChatService implements TelegramChatService {

    private final ChatRepository chatRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public void registerChat(Long chatId) {
        if (chatRepository.isExists(chatId)) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        chatRepository.add(chatId);
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        if (!chatRepository.isExists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        List<Link> links = chatLinkRepository.findAllLinkByChatId(chatId);
        chatLinkRepository.removeAllByChatId(chatId);
        links.forEach(link -> {
            if (chatLinkRepository.findAllChatByLinkId(link.linkId()).isEmpty()) {
                linkRepository.remove(link.linkId());
            }
        });
        chatRepository.remove(chatId);
    }
}
