package edu.java.repository.jooq;

import edu.java.dto.Chat;
import edu.java.repository.ChatRepository;
import java.util.List;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.Tables.CHAT;

public class JooqChatRepository implements ChatRepository {

    private final DSLContext dslContext;

    public JooqChatRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Chat> findAll() {
        return dslContext.select(CHAT.fields())
            .from(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public void add(long chatId) {
        dslContext.insertInto(CHAT, CHAT.CHAT_ID)
            .values(chatId)
            .execute();
    }

    @Override
    public void remove(long chatId) {
        dslContext.delete(CHAT)
            .where(CHAT.CHAT_ID.eq(chatId))
            .execute();
    }

    @Override
    public boolean isExists(long chatId) {
        return dslContext.fetchCount(CHAT, CHAT.CHAT_ID.eq(chatId)) > 0;
    }
}
