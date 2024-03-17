package edu.java.scheduler;

import edu.java.client.bot.BotClient;
import edu.java.client.bot.dto.request.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.Chat;
import edu.java.provider.InfoProviders;
import edu.java.provider.api.Info;
import edu.java.provider.api.InfoProvider;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkService linkService;

    private final ApplicationConfig applicationConfig;

    private final InfoProviders infoProviders;

    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.info("Updating links start");
        linkService.getOldLinks(
                applicationConfig.scheduler().forceCheckDelay(),
                applicationConfig.scheduler().maxLinksPerCheck()
            )
            .forEach(link -> {
                log.info("Updating link {}", link);
                String host = link.url().getHost();
                String domain = host.replaceAll("^(www\\.)?|\\.com$", "");
                InfoProvider provider = infoProviders.getSupplierByTypeHost(domain);
                Info linkInfo = provider.getInfo(link.url());

                if (linkInfo.lastModified().isAfter(link.lastUpdate())) {
                    linkService.update(link.linkId(), link.lastUpdate());
                    botClient.handleUpdate(new LinkUpdate(
                        link.linkId(),
                        link.url(),
                        linkInfo.title(),
                        linkService.getLinkSubscribers(link.url()).chats().stream()
                            .map(Chat::chatId)
                            .toList()
                    ));
                }
            });
        log.info("Updating links ends");
    }

}

