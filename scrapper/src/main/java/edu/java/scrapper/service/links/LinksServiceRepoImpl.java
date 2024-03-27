package edu.java.scrapper.service.links;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.model.ChatLink;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import java.time.Duration;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
public class LinksServiceRepoImpl extends LinksService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final ChatRepository chatRepository;

    public LinksServiceRepoImpl(
        UpdateInfoServiceProvider updateInfoServiceProvider,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        ChatRepository chatRepository
    ) {
        super(updateInfoServiceProvider);
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public LinksListResponse getLinksForChatId(Long chatId) {
        log.info("Get links for chat {}", chatId);
        checkChatRegistered(chatId);
        var chatLinks = chatLinkRepository.findAllByChatId(chatId);
        List<LinkResponse> linkResponses = chatLinks.stream()
            .map(
                chatLink -> linkRepository
                    .findById(chatLink.linkId())
                    .orElseThrow(LinkNotFoundException::new)
                    .toLinkResponse()
            ).toList();
        return new LinksListResponse(linkResponses, linkResponses.size());
    }

    @Override
    @Transactional
    public LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest) {
        var url = addLinkRequest.link();
        log.info("Add link {} to chat {}", url, chatId);
        checkChatRegistered(chatId);
        var lastUpdate = getLastUpdate(url);
        var link = linkRepository.add(url, lastUpdate);
        log.info("Link added to database: {}", link);
        if (chatLinkRepository.existsByChatIdAndLinkId(chatId, link.id())) {
            log.info("Link {} already tracked in chat {}", url, chatId);
            throw new LinkAlreadyTrackedException();
        }
        log.info("Adding link {} to chat {}", url, chatId);
        chatLinkRepository.add(chatId, link.id());
        log.info("Added link {} to chat {}", url, chatId);
        return link.toLinkResponse();
    }

    @Override
    @Transactional
    public LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Remove link {} from chat {}", removeLinkRequest.link(), chatId);
        checkChatRegistered(chatId);
        var link = linkRepository.findByUrl(removeLinkRequest.link())
            .orElseThrow(LinkNotFoundException::new); // no such link in links
        chatLinkRepository.remove(chatId, link.id())
            .orElseThrow(LinkNotFoundException::new); // no such link in chat links
        if (chatLinkRepository.findAllByLinkId(link.id()).isEmpty()) { // if no chats with this link
            linkRepository.removeById(link.id()); // remove link
            log.info("Removed link {}", removeLinkRequest.link());
        }
        log.info("Removed link {} from chat {}", removeLinkRequest.link(), chatId);
        return new LinkResponse(chatId, link.url());
    }

    @Override
    @Transactional
    public void updateLink(Link link) {
        log.info("Update link {}", link.url());
        linkRepository.update(link);
        log.info("Updated link {}", link.url());
    }

    @Override
    @Transactional
    public List<Long> getChatIdsForLink(Link link) {
        log.info("Get chat ids for link {}", link.url());
        return chatLinkRepository
            .findAllByLinkId(link.id())
            .stream()
            .map(ChatLink::chatId)
            .toList();
    }

    @Override
    public List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck) {
        log.info("List stale links");
        return linkRepository
            .findWithTimeSinceLastCheckLessThanGivenOrderedByLastCheckTime(
                limit, minTimeSinceLastCheck
            );
    }

    void checkChatRegistered(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            log.info("Chat {} not registered", chatId);
            throw new ChatNotRegisteredException();
        }
    }
}
