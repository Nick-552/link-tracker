package edu.java.scrapper.service.scheduler;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.links.LinksService;
import edu.java.scrapper.service.update.UpdateInfo;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdatesScheduler {

    private final LinksService linksService;
    private final ApplicationConfig applicationConfig;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}", initialDelayString = "${app.scheduler.initial-delay}")
    public void update() {
        log.info("Updating...");
        var links = linksService.listStaleLinks(
            applicationConfig.scheduler().checkLimit(),
            Duration.ofMinutes(applicationConfig.scheduler().forceCheckDelay())
        );
        links.forEach(link -> {
            try {
                updateLink(link);
            } catch (Exception e) {
                log.info("Failed to update {}", link.url(), e);
            }
        });
        log.info("Update finished");
    }

    public void updateLink(Link link) {
        var lastRealUpdate = linksService.getLastUpdate(link.url());
        log.info("Checking update for {}", link.url());
        Link updatedLink = link.getUpdated(lastRealUpdate);
        if (lastRealUpdate.isAfter(link.lastCheckAt()) || lastRealUpdate.isAfter(link.lastUpdatedAt())) {
            log.info("Updating {}", link.url());
            var updateInfo = linksService.getUpdateInformation(link.url(), link.lastUpdatedAt());
            sendUpdateInfo(link, updateInfo);
        }
        linksService.updateLink(updatedLink);
    }

    public void sendUpdateInfo(Link link, UpdateInfo updateInfo) {
        for (var event : updateInfo.events()) {
            var update = new LinkUpdate(
                link.id(),
                link.url(),
                event.eventTime().toString(),
                event.eventDescription(),
                linksService.getChatIdsForLink(link)
            );
            botClient.sendUpdate(update);
        }
    }
}
