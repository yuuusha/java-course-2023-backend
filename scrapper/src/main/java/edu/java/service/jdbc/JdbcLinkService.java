package edu.java.service.jdbc;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListChatsResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exception.LinkIsNotSupportedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import edu.java.service.LinkService;
import edu.java.supplier.InfoSuppliers;
import edu.java.supplier.api.InfoSupplier;
import edu.java.supplier.api.LinkInfo;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final ChatLinkRepository chatLinkRepository;

    private final LinkRepository linkRepository;

    private final InfoSuppliers infoSuppliers;

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
        InfoSupplier infoSupplier = infoSuppliers.getSupplierByTypeHost(domain);
        if (infoSupplier == null || !infoSupplier.isSupported(link)) {
            throw new LinkIsNotSupportedException(link);
        }

        LinkInfo linkInfo = infoSupplier.fetchInfo(link);
        if (linkInfo == null) {
            throw new LinkIsNotSupportedException(link);
        }

        Long linkId;
        if (linkInfo.events().isEmpty()) {
            linkId = linkRepository.add(new Link(1L,
                linkInfo.url(),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                linkInfo.metaInfo()
            ));
        } else {
            linkId = linkRepository.add(new Link(1L,
                linkInfo.url(),
                linkInfo.events().getFirst().lastUpdate(),
                OffsetDateTime.now(),
                linkInfo.metaInfo()
            ));
        }
        chatLinkRepository.add(tgChatId, linkId);
        return new LinkResponse(linkId, link);
    }

    @Override
    @Transactional
    public LinkResponse deleteTrackingLink(URL url, Long tgChatId) {
        Optional<Link> link = linkRepository.findByUrl(url);
        if (link.isPresent()) {
            long linkId = link.get().linkId();
            chatLinkRepository.remove(tgChatId, linkId);
            if (chatLinkRepository.findAllChatByLinkId(linkId).isEmpty()) {
                linkRepository.remove(linkId);
            }
            return new LinkResponse(linkId, url);
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
    public void update(Long id, OffsetDateTime lastUpdate, String metaInfo) {
        if (linkRepository.findById(id) == null) {
            throw new LinkNotFoundException(id);
        }
        linkRepository.update(id, lastUpdate, metaInfo);
    }

    @Override
    @Transactional
    public ListChatsResponse getLinkSubscribers(URL url) {
        List<Chat> chats = chatLinkRepository.findAllChatByLinkUrl(url);
        return new ListChatsResponse(chats, chats.size());
    }

    @Override
    public void checkNow(Long id) {
        linkRepository.checkNow(id);
    }
}
