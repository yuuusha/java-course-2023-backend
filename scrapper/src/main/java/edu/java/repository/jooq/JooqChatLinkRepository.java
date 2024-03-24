package edu.java.repository.jooq;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.repository.ChatLinkRepository;
import java.net.URL;
import java.util.List;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.Tables.CHAT;
import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;

public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext dslContext;

    public JooqChatLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void add(Long chatId, Long linkId) {
        dslContext.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
            .values(chatId, linkId)
            .execute();
    }

    @Override
    public void remove(Long chatId, Long linkId) {
        dslContext.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId)))
            .execute();
    }

    @Override
    public List<Link> findAllLinkByChatId(Long chatId) {
        return dslContext.select(LINK.fields()).from(CHAT_LINK)
            .join(LINK)
            .on(CHAT_LINK.LINK_ID.eq(LINK.LINK_ID))
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    @Override
    public List<Chat> findAllChatByLinkUrl(URL url) {
        return dslContext.select(CHAT.fields()).from(CHAT_LINK)
            .join(CHAT)
            .on(CHAT_LINK.CHAT_ID.eq(CHAT.CHAT_ID))
            .join(LINK)
            .on(CHAT_LINK.LINK_ID.eq(LINK.LINK_ID))
            .where(LINK.URL.eq(url.toString()))
            .fetchInto(Chat.class);
    }

    @Override
    public List<Chat> findAllChatByLinkId(Long linkId) {
        return dslContext.select(CHAT.fields()).from(CHAT_LINK)
            .join(CHAT)
            .on(CHAT_LINK.CHAT_ID.eq(CHAT.CHAT_ID))
            .where(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(Chat.class);
    }

    @Override
    public void removeAllByChatId(Long chatId) {
        dslContext.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId))
            .execute();
    }

    @Override
    public boolean isExists(Long chatId, Long linkId) {
        return dslContext.fetchCount(CHAT_LINK, CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(linkId))) > 0;
    }

    @Override
    public boolean isExists(Long chatId, URL url) {
        return dslContext.fetchCount(CHAT_LINK.join(LINK).on(CHAT_LINK.LINK_ID.eq(LINK.LINK_ID)),
            CHAT_LINK.CHAT_ID.eq(chatId).and(LINK.URL.eq(url.toString()))) > 0;
    }

}
