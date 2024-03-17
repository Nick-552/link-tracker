package edu.java.scrapper.service.links;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.model.ChatLink;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.repository.jdbc.ChatLinkRepository;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.repository.jdbc.LinkRepository;
import edu.java.scrapper.util.LinkResolver;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@RequiredArgsConstructor
public class LinksServiceJdbcImpl implements LinksService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final ChatRepository chatRepository;


    @Override
    public LinksListResponse getLinksForChatId(Long chatId) {
        log.info("Get links for chat {}", chatId);
        checkChatRegistered(chatId);
        var chatLinks = chatLinkRepository.findAllByChatId(chatId);
        var links = chatLinks.stream()
            .map(chatLink -> linkRepository.findById(chatLink.linkId()).toLinkResponse())
            .toList();
        log.info("Found {} links for chat {}", links.size(), chatId);
        return new LinksListResponse(links, links.size());
    }

    @Override
    public LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest) {
        log.info("Add link {} to chat {}", addLinkRequest.link(), chatId);
        try {
            var linkType = LinkResolver.getLinkType(addLinkRequest.link());
        } catch (Exception e) {
            throw new UnsupportedUrlException();
        }
        checkChatRegistered(chatId);
        var link = linkRepository.add(addLinkRequest.link(), OffsetDateTime.now());
        try {
            chatLinkRepository.add(chatId, link.id());
        } catch (DataIntegrityViolationException e) {
            throw new LinkAlreadyTrackedException();
        }
        log.info("Added link {} to chat {}", addLinkRequest.link(), chatId);
        return link.toLinkResponse();
    }

    @Override
    public LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Remove link {} from chat {}", removeLinkRequest.link(), chatId);
        checkChatRegistered(chatId);
        try {
            var link = linkRepository.findByUrl(removeLinkRequest.link()); // find link
            if (chatLinkRepository.remove(chatId, link.id()) == null) { // remove chat link
                throw new LinkNotFoundException();
            }
            if (chatLinkRepository.findAllByLinkId(link.id()).isEmpty()) { // if no chat links
                linkRepository.remove(link.id()); // remove link
            }
            log.info("Removed link {} from chat {}", removeLinkRequest.link(), chatId);
            return link.toLinkResponse();
        } catch (EmptyResultDataAccessException e) {
            throw new LinkNotFoundException();
        }
    }

    @Override
    public void updateLink(Link link) {
        log.info("Update link {}", link.url());
        linkRepository.update(link);
        log.info("Updated link {}", link.url());
    }

    @Override
    public List<Long> getChatIdsForLink(Link link) {
        log.info("Get chat ids for link {}", link.url());
        var chatIds = chatLinkRepository.findAllByLinkId(link.id()).stream()
            .map(ChatLink::chatId)
            .toList();
        log.info("Found {} chat ids for link {}", chatIds.size(), link.url());
        return chatIds;
    }

    @Override
    public List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck) {
        log.info("List stale links");
        var links = linkRepository
            .findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
                limit, minTimeSinceLastCheck
            );
        log.info("Found {} stale links", links.size());
        return links;
    }

    private void checkChatRegistered(Long chatId) {
        try {
            log.info("Check chat {}", chatId);
            chatRepository.findById(chatId);
            log.info("Chat {} is registered", chatId);
        } catch (EmptyResultDataAccessException e) {
            throw new ChatNotRegisteredException();
        }
    }
}
