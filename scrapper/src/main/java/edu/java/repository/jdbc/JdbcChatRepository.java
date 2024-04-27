package edu.java.repository.jdbc;

import edu.java.dto.Chat;
import edu.java.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcClient client;

    @Override
    public List<Chat> findAll() {
        return client.sql("SELECT (chat_id) FROM chat")
            .query(Chat.class)
            .list();
    }

    @Override
    public void add(long chatId) {
        client.sql("INSERT INTO chat(chat_id) VALUES (?)")
            .param(chatId)
            .update();
    }

    @Override
    public void remove(long chatId) {
        client.sql("DELETE FROM chat WHERE chat_id = ?")
            .param(chatId)
            .update();
    }

    @Override
    public boolean isExists(long chatId) {
        return client.sql("SELECT chat_id FROM chat WHERE chat_id = ?")
            .param(chatId)
            .query(Chat.class)
            .optional()
            .isPresent();
    }
}
