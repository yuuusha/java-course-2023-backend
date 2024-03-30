package edu.java.service.jpa;

import edu.java.domain.jpa.entity.LinkEntity;
import edu.java.domain.jpa.entity.TgChatEntity;
import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListChatsResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkIsNotSupportedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.service.LinkService;
import edu.java.supplier.InfoSuppliers;
import edu.java.supplier.api.InfoSupplier;
import edu.java.supplier.api.LinkInfo;
import edu.java.util.URLCreator;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;

    private final JpaChatRepository chatRepository;

    private final InfoSuppliers infoSuppliers;

    @Override
    @Transactional
    public ListLinksResponse getTrackedLinks(Long tgChatId) {
        TgChatEntity tgChat = chatRepository.findById(tgChatId).orElseThrow(() -> new ChatNotFoundException(tgChatId));
        List<LinkResponse> linkResponses =
            tgChat.getLinks()
                .stream()
                .map(linkEntity -> new LinkResponse(linkEntity.getId(), URLCreator.createURL(linkEntity.getUrl())))
                .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    @Transactional
    public LinkResponse addTrackingLink(URL link, Long chatId) {
        TgChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId));
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

        Optional<LinkEntity> linkOptional;
        OffsetDateTime lastUpdate = OffsetDateTime.now();
        if (!linkInfo.events().isEmpty()) {
            lastUpdate = linkInfo.events().getFirst().lastUpdate();
        }

        LinkEntity linkEntity;
        linkOptional = linkRepository.findByUrl(String.valueOf(linkInfo.url()));
        if (linkOptional.isPresent()) {
            linkEntity = linkOptional.get();
            linkEntity.setLastUpdate(lastUpdate);
            linkEntity.setLastCheck(OffsetDateTime.now());
            linkEntity.setMetaInfo(linkInfo.metaInfo());
            linkRepository.save(linkEntity);
            chat.addLink(linkEntity);
            return new LinkResponse(linkEntity.getId(), link);
        }
        linkEntity = new LinkEntity(
            String.valueOf(link),
            lastUpdate,
            OffsetDateTime.now(),
            linkInfo.metaInfo()
        );
        linkRepository.save(linkEntity);
        chat.addLink(linkEntity);
        return new LinkResponse(linkEntity.getId(), link);
    }

    @Override
    @Transactional
    public LinkResponse deleteTrackingLink(URL url, Long chatId) {
        TgChatEntity chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId));
        LinkEntity link =
            linkRepository.findByUrl(String.valueOf(url)).orElseThrow(() -> new LinkNotFoundException(url));
        chat.removeLink(link);
        if (link.getTgChats().isEmpty()) {
            linkRepository.delete(link);
        }
        return new LinkResponse(link.getId(), url);
    }

    @Override
    @Transactional
    public List<Link> getOldLinks(Duration afterDuration, int limit) {
        return linkRepository.findAllByLastCheckBefore(OffsetDateTime.now().minus(afterDuration), Limit.of(limit))
            .stream().map(LinkEntity::toDto).toList();
    }

    @Override
    @Transactional
    public void update(Long id, OffsetDateTime lastUpdate, String metaInfo) {
        LinkEntity linkEntity = linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
        linkEntity.setLastUpdate(lastUpdate);
        linkEntity.setMetaInfo(metaInfo);
    }

    @Override
    @Transactional
    public ListChatsResponse getLinkSubscribers(URL url) {
        LinkEntity linkEntity =
            linkRepository.findByUrl(String.valueOf(url)).orElseThrow(() -> new LinkNotFoundException(url));
        List<Chat> chatList = linkEntity.getTgChats().stream().map(chat -> new Chat(chat.getId())).toList();
        return new ListChatsResponse(chatList, chatList.size());
    }

    @Override
    @Transactional
    public void checkNow(Long linkId) {
        LinkEntity linkEntity = linkRepository.findById(linkId).orElseThrow(() -> new ChatNotFoundException(linkId));
        linkEntity.setLastCheck(OffsetDateTime.now());
    }
}
