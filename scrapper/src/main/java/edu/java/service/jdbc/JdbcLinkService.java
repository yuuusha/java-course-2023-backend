package edu.java.service.jdbc;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListChatsResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exception.LinkIsNotSupportedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.provider.InfoProviders;
import edu.java.provider.api.Info;
import edu.java.provider.api.InfoProvider;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.service.LinkService;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final ChatLinkRepository chatLinkRepository;

    private final LinkRepository linkRepository;

    private final InfoProviders infoProviders;

    @Override
    @Transactional
    public ListLinksResponse getTrackedLinks(Long tgChatId) {
        List<Link> lisks = chatLinkRepository.findAllLinkByChatId(tgChatId);
        List<LinkResponse> linkResponses =
            lisks.stream().map(link -> new LinkResponse(link.linkId(), link.url())).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    @Transactional
    public LinkResponse addTrackingLink(URL link, Long tgChatId) {
        String host = link.getHost();
        String domain = host.replaceAll("^(www\\.)?|\\.com$", "");
        InfoProvider infoProvider = infoProviders.getSupplierByTypeHost(domain);
        if (infoProvider == null || !infoProvider.isSupported(link)) {
            throw new LinkIsNotSupportedException(link);
        }

        Info linkInfo = infoProvider.getInfo(link);
        if (linkInfo == null) {
            throw new LinkIsNotSupportedException(link);
        }

        Long linkId;
        if (linkInfo.lastModified() == null) {
            linkId = linkRepository.add(new Link(1L, linkInfo.url(), OffsetDateTime.now(), OffsetDateTime.now()));
        } else {
            linkId = linkRepository.add(new Link(1L, linkInfo.url(), linkInfo.lastModified(), OffsetDateTime.now()));
        }
        chatLinkRepository.add(tgChatId, linkId);
        return new LinkResponse(linkId, link);
    }

    @Override
    @Transactional
    public LinkResponse deleteTrackingLink(URL url, Long tgChatId) {
        Link link = linkRepository.findByUrl(url);
        if (link != null) {
            chatLinkRepository.remove(tgChatId, link.linkId());
            if (chatLinkRepository.findAllChatByLinkId(link.linkId()).isEmpty()) {
                linkRepository.remove(link.linkId());
            }
            return new LinkResponse(link.linkId(), url);
        } else {
            throw new LinkNotFoundException(url);
        }
    }

    @Override
    @Transactional
    public List<Link> getOldLinks(Duration afterDuration, int limit) {
        return linkRepository.findLinksCheckedAfter(afterDuration, limit);
    }

    @Override
    @Transactional
    public void update(Long id, OffsetDateTime lastUpdate) {
        if (linkRepository.findById(id) == null) {
            throw new LinkNotFoundException(id);
        }
        linkRepository.update(id, lastUpdate);
    }

    @Override
    @Transactional
    public ListChatsResponse getLinkSubscribers(URL url) {
        List<Chat> chats = chatLinkRepository.findAllChatByLinkUrl(url);
        return new ListChatsResponse(chats, chats.size());
    }
}
