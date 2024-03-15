package edu.java.scrapper.service.links;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.model.ChatLink;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.repository.jdbc.ChatLinkRepository;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.repository.jdbc.LinkRepository;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinksServiceJdbcImpl implements LinksService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final ChatRepository chatRepository;


    @Override
    public LinksListResponse getLinksForChatId(Long chatId) {
        checkChatRegistered(chatId);
        var chatLinks = chatLinkRepository.findAllByChatId(chatId);
        var links = chatLinks.stream()
            .map(chatLink -> linkRepository.findById(chatLink.linkId()).toLinkResponse())
            .toList();
        return new LinksListResponse(links, links.size());
    }

    @Override
    public LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest) {
        checkChatRegistered(chatId);
        var link = linkRepository.add(addLinkRequest.link(), OffsetDateTime.now());
        chatLinkRepository.add(chatId, link.id());
        return link.toLinkResponse();
    }

    @Override
    public LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest) {
        checkChatRegistered(chatId);
        try {
            var link = linkRepository.findByUrl(removeLinkRequest.link()); // find link
            if (chatLinkRepository.remove(chatId, link.id()) == 0) { // remove chat link
                throw new LinkNotFoundException();
            }
            if (chatLinkRepository.findAllByLinkId(link.id()).isEmpty()) { // if no chat links
                linkRepository.remove(link.id()); // remove link
            }
            return link.toLinkResponse();
        } catch (EmptyResultDataAccessException e) {
            throw new LinkNotFoundException();
        }
    }

    @Override
    public void updateLink(Link link) {
        linkRepository.update(link);
    }

    @Override
    public List<Long> getChatIdsForLink(Link link) {
        return chatLinkRepository.findAllByLinkId(link.id()).stream()
            .map(ChatLink::chatId)
            .toList();
    }

    @Override
    public List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck) {
        return linkRepository.findAllOrderedByLastCheckTime(limit, minTimeSinceLastCheck);
    }

    private void checkChatRegistered(Long chatId) {
        try {
            chatRepository.findById(chatId);
        } catch (EmptyResultDataAccessException e) {
            throw new ChatNotRegisteredException();
        }
    }
}
