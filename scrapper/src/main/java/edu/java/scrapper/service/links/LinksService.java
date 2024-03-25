package edu.java.scrapper.service.links;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.update.UpdateInfo;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class LinksService {

    protected final UpdateInfoServiceProvider updateInfoServiceProvider;

    public abstract LinksListResponse getLinksForChatId(Long chatId);

    public abstract LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest);

    public abstract LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest);

    public abstract void updateLink(Link link);

    public abstract List<Long> getChatIdsForLink(Link link);

    public abstract List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck);

    public OffsetDateTime getLastUpdate(URI url) {
        var updateInfoService = updateInfoServiceProvider.provide(url);
        return updateInfoService.getLastUpdate(url);
    }

    public UpdateInfo getUpdateInformation(URI url) {
        var updateInfoService = updateInfoServiceProvider.provide(url);
        return updateInfoService.getUpdateInformation(url);
    }
}
