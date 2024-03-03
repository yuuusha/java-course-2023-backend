package edu.java.bot.service;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static edu.java.bot.service.URLService.createURL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultBotServiceTest {

    @Mock
    private ScrapperClient scrapperClient;

    @InjectMocks
    private DefaultBotService botService;

    private Long chatId;

    URL url;
    @BeforeEach
    void setUp() {
        chatId = 42L;
        url = createURL("https://google.com");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addTrackingLinkTest() {
        AddLinkRequest expectedRequest = new AddLinkRequest(url);
        LinkResponse expectedResponse = new LinkResponse(chatId, url);

        when(scrapperClient.addTrackingLink(chatId, expectedRequest)).thenReturn(expectedResponse);

        LinkResponse actualResponse = botService.addLink(chatId, String.valueOf(url));

        assertEquals(expectedResponse, actualResponse);
        verify(scrapperClient, times(1)).addTrackingLink(chatId, expectedRequest);
    }

    @Test
    void deleteTrackingLinkTest() {
        RemoveLinkRequest expectedRequest = new RemoveLinkRequest(url);
        LinkResponse expectedResponse = new LinkResponse(chatId, url);

        when(scrapperClient.deleteTrackingLink(chatId, expectedRequest)).thenReturn(expectedResponse);

        LinkResponse actualResponse = botService.removeLink(chatId, String.valueOf(url));

        assertEquals(expectedResponse, actualResponse);
        verify(scrapperClient, times(1)).deleteTrackingLink(chatId, expectedRequest);
    }

    @Test
    void getTrackedLinksTest() {
        ListLinksResponse expectedResponse = new ListLinksResponse(List.of());

        when(scrapperClient.getTrackedLinks(chatId)).thenReturn(expectedResponse);

        ListLinksResponse actualResponse = botService.getListLinks(chatId);

        assertEquals(expectedResponse, actualResponse);
        verify(scrapperClient, times(1)).getTrackedLinks(chatId);
    }

}
