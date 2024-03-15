package edu.java.scrapper.service.links;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.model.Link;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface LinksService {

    @Transactional
    LinksListResponse getLinksForChatId(Long chatId);

    @Transactional
    LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest);

    @Transactional
    LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest);

    @Transactional
    void updateLink(Link link);

    @Transactional
    List<Long> getChatIdsForLink(Link link);

    @Transactional List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck);
}
