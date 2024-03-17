package edu.java.scrapper.service.scheduler;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.request.bot.LinkUpdate;
import edu.java.scrapper.service.links.LinksService;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
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
public class LinkUpdaterScheduler {

    private final UpdateInfoServiceProvider updateInfoServiceProvider;
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
            var infoService = updateInfoServiceProvider.provide(link.url());
            var lastRealUpdate = infoService.getLastUpdate(link.url());
            log.info("Checking update for {}", link.url());
            if (lastRealUpdate.isAfter(link.lastCheckAt()) || lastRealUpdate.isAfter(link.lastUpdatedAt())) {
                log.info("Updating {}", link.url());
                var updateInfo = infoService.getUpdateInformation(link.url());
                var update = new LinkUpdate(
                    link.id(),
                    link.url(),
                    updateInfo.lastUpdate().toString(),
                    updateInfo.message(),
                    linksService.getChatIdsForLink(link)
                );
                var updated = link.getUpdated(lastRealUpdate);
                botClient.sendUpdate(update);
                linksService.updateLink(updated);
            }
        });
        log.info("Update finished");
    }
}
