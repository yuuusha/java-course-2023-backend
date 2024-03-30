package edu.java.scrapper.service.jdbc;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.response.ListChatsResponse;
import edu.java.exception.LinkNotFoundException;
import edu.java.repository.jdbc.JdbcChatLinkRepository;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.LinkService;
import edu.java.service.TelegramChatService;
import edu.java.supplier.InfoSuppliers;
import edu.java.supplier.api.LinkInfo;
import edu.java.supplier.github.GithubInfoSupplier;
import edu.java.util.URLCreator;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcLinkServiceTest extends IntegrationEnvironment {

    @Autowired
    private LinkService linkService;

    @Autowired
    private TelegramChatService chatService;

    @Autowired
    private JdbcChatRepository chatRepository;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;

    @MockBean
    private GithubInfoSupplier supplier;

    @MockBean
    private InfoSuppliers suppliers;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        String url = "https://github.com/yuuusha/java-course-2023-backend";
        Mockito.when(supplier.fetchInfo(Mockito.any()))
            .thenReturn(new LinkInfo(URLCreator.createURL(url), "github", List.of(), ""));
        Mockito.when(supplier.isSupported(Mockito.any())).thenReturn(true);
        Mockito.when(suppliers.getSupplierByTypeHost(Mockito.any())).thenReturn(supplier);
        chatService.registerChat(41L);

        var response = linkService.addTrackingLink(URLCreator.createURL(url), 41L);
        Assertions.assertThat(linkRepository.findById(response.id()).get().url().toString())
            .isEqualTo(url);
        Assertions.assertThat(chatLinkRepository.findAllLinkByChatId(41L)).map(Link::url)
            .contains(URLCreator.createURL(url));
    }

    @Test
    @Transactional
    @Rollback
    void getSubscribersTest() {
        chatRepository.add(41L);
        URL url = URLCreator.createURL("https://github.com");
        Long linkId = linkRepository.add(new Link(0L, url, OffsetDateTime.now(), OffsetDateTime.now(), ""));
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

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }
}
