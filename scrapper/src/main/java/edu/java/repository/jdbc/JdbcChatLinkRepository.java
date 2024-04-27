package edu.java.repository.jdbc;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.repository.ChatLinkRepository;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcClient client;

    @Override
    public void add(Long chatId, Long linkId) {
        client.sql("INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)").params(List.of(chatId, linkId)).update();
    }

    public void remove(Long chatId, Long linkId) {
        client.sql("DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?").params(List.of(chatId, linkId)).update();
    }

    public List<Link> findAllLinkByChatId(Long chatId) {
        return client.sql(
                """
                    SELECT l.* FROM chat_link
                    INNER JOIN public.link l ON chat_link.link_id = l.link_id
                    WHERE chat_id = ?""")
            .param(chatId).query(Link.class).list();
    }

    public List<Chat> findAllChatByLinkUrl(URL url) {
        Long linkId = client.sql("SELECT link_id FROM link where url = ?")
            .param(String.valueOf(url))
            .query(Long.class)
            .single();

        if (linkId == null) {
            return Collections.emptyList();
        }

        return findAllChatByLinkId(linkId);
    }

    @Override
    public List<Chat> findAllChatByLinkId(Long linkId) {
        return client.sql(
                """
                    SELECT c.* FROM chat_link
                    INNER JOIN public.chat c on chat_link.chat_id = c.chat_id
                    WHERE link_id = ?
                    """)
            .param(linkId).query(Chat.class).list();
    }

    @Override
    public void removeAllByChatId(Long chatId) {
        client.sql("DELETE FROM chat_link WHERE chat_id = ?")
            .params(chatId)
            .update();
    }

    public boolean isExists(Long chatId, Long linkId) {
        return client.sql(
                """
                    SELECT chat_id FROM chat_link
                    WHERE chat_id = ? AND link_id = ?
                    """)
            .params(chatId, linkId)
            .query(Long.class)
            .optional()
            .isPresent();
    }

    @Override
    public boolean isExists(Long chatId, URL url) {
        Long linkId = client.sql("SELECT link_id FROM link WHERE url = ?")
            .param(url)
            .query(Long.class)
            .optional()
            .orElse(null);

        if (linkId == null) {
            return false;
        }

        return isExists(chatId, linkId);
    }
}
