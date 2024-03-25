package edu.java.scrapper.service.links;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.model.Link;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface LinksService {

    LinksListResponse getLinksForChatId(Long chatId);

    LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest);

    void updateLink(Link link);

    List<Long> getChatIdsForLink(Link link);

    List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck);
}
