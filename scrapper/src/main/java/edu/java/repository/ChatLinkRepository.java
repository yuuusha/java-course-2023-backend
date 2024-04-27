package edu.java.repository;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import java.net.URL;
import java.util.List;

public interface ChatLinkRepository {

    void add(Long chatId, Long linkId);

    void remove(Long chatId, Long linkId);

    List<Link> findAllLinkByChatId(Long chatId);

    List<Chat> findAllChatByLinkUrl(URL url);

    List<Chat> findAllChatByLinkId(Long linkId);

    void removeAllByChatId(Long chatId);

    boolean isExists(Long chatId, Long linkId);

    boolean isExists(Long chatId, URL url);
}
