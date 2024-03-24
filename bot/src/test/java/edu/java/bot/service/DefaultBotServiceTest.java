package edu.java.bot.service;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import edu.java.bot.client.scrapper.dto.response.LinkResponse;
import edu.java.bot.client.scrapper.dto.response.ListLinksResponse;
import edu.java.bot.dto.OptionalAnswer;
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
        OptionalAnswer<LinkResponse> expectedResponse = new OptionalAnswer<>(new LinkResponse(chatId, url), null);

        when(scrapperClient.addTrackingLink(chatId, expectedRequest)).thenReturn(expectedResponse);

        OptionalAnswer<LinkResponse> actualResponse = botService.trackUserLink(chatId, String.valueOf(url));

        assertEquals(expectedResponse, actualResponse);
        verify(scrapperClient, times(1)).addTrackingLink(chatId, expectedRequest);
    }

    @Test
    void deleteTrackingLinkTest() {
        RemoveLinkRequest expectedRequest = new RemoveLinkRequest(url);
        OptionalAnswer<LinkResponse> expectedResponse = new OptionalAnswer<>(new LinkResponse(chatId, url), null);

        when(scrapperClient.deleteTrackingLink(chatId, expectedRequest)).thenReturn(expectedResponse);

        OptionalAnswer<LinkResponse> actualResponse = botService.unTrackUserLink(chatId, String.valueOf(url));

        assertEquals(expectedResponse, actualResponse);
        verify(scrapperClient, times(1)).deleteTrackingLink(chatId, expectedRequest);
    }

    @Test
    void getTrackedLinksTest() {
        OptionalAnswer<ListLinksResponse> expectedResponse =
            new OptionalAnswer<>(new ListLinksResponse(List.of()), null);

        when(scrapperClient.getTrackedLinks(chatId)).thenReturn(expectedResponse);

        OptionalAnswer<ListLinksResponse> actualResponse = botService.userLinks(chatId);

        assertEquals(expectedResponse, actualResponse);
        verify(scrapperClient, times(1)).getTrackedLinks(chatId);
    }

}
