package edu.java.scrapper.service.jpa;

import edu.java.dto.Chat;
import edu.java.dto.response.ListChatsResponse;
import edu.java.exception.LinkNotFoundException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.TelegramChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.supplier.InfoSuppliers;
import edu.java.supplier.api.LinkInfo;
import edu.java.supplier.github.GithubInfoSupplier;
import edu.java.util.URLCreator;
import java.net.URL;
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
public class JpaLinkServiceTest extends IntegrationEnvironment {

    @Autowired
    private JpaLinkService linkService;

    @Autowired
    private TelegramChatService chatService;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private JpaChatRepository chatRepository;

    @MockBean
    private GithubInfoSupplier supplier;

    @MockBean
    private InfoSuppliers suppliers;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        URL url = URLCreator.createURL("https://github.com/yuuusha/java-course-2023-backend");
        Mockito.when(supplier.fetchInfo(Mockito.any()))
            .thenReturn(new LinkInfo(url, "github", List.of(), ""));
        Mockito.when(supplier.isSupported(Mockito.any())).thenReturn(true);
        Mockito.when(suppliers.getSupplierByTypeHost(Mockito.any())).thenReturn(supplier);
        chatService.registerChat(41L);

        linkService.addTrackingLink(url, 41L);
        Assertions.assertThat(linkRepository.findByUrl(String.valueOf(url)).get().toDto().url())
            .isEqualTo(url);
    }

    @Test
    @Transactional
    @Rollback
    void getSubscribersTest() {
        chatService.registerChat(41L);
        URL url = URLCreator.createURL("https://github.com/yuuusha/java-course-2023-backend");
        Mockito.when(supplier.fetchInfo(Mockito.any()))
            .thenReturn(new LinkInfo(url, "github", List.of(), ""));
        Mockito.when(supplier.isSupported(Mockito.any())).thenReturn(true);
        Mockito.when(suppliers.getSupplierByTypeHost(Mockito.any())).thenReturn(supplier);
        linkService.addTrackingLink(url, 41L);

        ListChatsResponse linkSubscribers = linkService.getLinkSubscribers(url);
        Assertions.assertThat(linkSubscribers).extracting(ListChatsResponse::chats).isEqualTo(List.of(new Chat(41L)));
    }

    @Test
    @Transactional
    @Rollback
    void removeNotExistedLinkTests() {
        chatService.registerChat(41L);

        Assertions.assertThatThrownBy(() -> linkService.deleteTrackingLink(
                URLCreator.createURL("https://github.com"),
                41L
            ))
            .isInstanceOf(LinkNotFoundException.class);
    }

    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }
}
