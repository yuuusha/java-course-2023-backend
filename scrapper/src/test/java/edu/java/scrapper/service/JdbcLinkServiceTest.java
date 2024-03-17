package edu.java.scrapper.service;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListChatsResponse;
import edu.java.exception.LinkNotFoundException;
import edu.java.provider.InfoProviders;
import edu.java.provider.sources.GithubInfoProvider;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.util.URLCreator;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcLinkServiceTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;

    @Autowired
    private TelegramChatService chatService;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;

    @Autowired
    private GithubInfoProvider provider;

    @Autowired
    private InfoProviders infoProviders;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        String urlString = "https://github.com/AndrewSalygin/java-course-2023-backend";
        chatService.registerChat(41L);
        URL url = URLCreator.createURL(urlString);

        LinkResponse linkResponse = linkService.addTrackingLink(url, 41L);
        Assertions.assertThat(linkRepository.findById(linkResponse.id()).url()).isEqualTo(url);
        Assertions.assertThat(chatLinkRepository.findAllLinkByChatId(41L)).map(Link::url).contains(url);
    }

    @Test
    @Transactional
    @Rollback
    void getSubscribersTest() {
        chatService.registerChat(41L);
        URL url = URLCreator.createURL("https://github.com");
        Long linkId = linkRepository.add(new Link(0L, url, OffsetDateTime.now(), OffsetDateTime.now()));
        chatLinkRepository.add(41L, linkId);

        ListChatsResponse linkSubscribers = linkService.getLinkSubscribers(url);
        Assertions.assertThat(linkSubscribers).extracting(ListChatsResponse::chats).isEqualTo(List.of(new Chat(41L)));
    }

    @Test
    @Transactional
    @Rollback
    void removeNotExistedLinkTests() {
        chatService.registerChat(41L);

        Assertions.assertThatThrownBy(() -> linkService.deleteTrackingLink(URLCreator.createURL("https://github.com"), 41L))
            .isInstanceOf(LinkNotFoundException.class);
    }

}
