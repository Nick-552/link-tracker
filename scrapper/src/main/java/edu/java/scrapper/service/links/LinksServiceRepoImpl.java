package edu.java.scrapper.service.links;

import edu.java.scrapper.domain.ChatLinkRepository;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.model.ChatLink;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import java.time.Duration;
import java.util.List;
import lombok.extern.log4j.Log4j2;

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
    public LinksListResponse getLinksForChatId(Long chatId) {
        log.info("Get links for chat {}", chatId);
        checkChatRegistered(chatId);
        var chatLinks = chatLinkRepository.findAllByChatId(chatId);
        List<LinkResponse> linkResponses = chatLinks.stream()
            .map(
                chatLink -> linkRepository
                    .findById(chatLink.linkId())
                    .toLinkResponse()
            ).toList();
        return new LinksListResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest) {
        var url = addLinkRequest.link();
        log.info("Add link {} to chat {}", url, chatId);
        checkChatRegistered(chatId);
        var updateInfoService = updateInfoServiceProvider.provide(url);
        var lastUpdate = updateInfoService.getLastUpdate(url);
        var link = linkRepository.add(url, lastUpdate);
        if (chatLinkRepository.existsByChatIdAndLinkId(chatId, link.id())) {
            log.info("Link {} already tracked in chat {}", url, chatId);
            throw new LinkAlreadyTrackedException();
        }
        chatLinkRepository.add(chatId, link.id());
        log.info("Added link {} to chat {}", url, chatId);
        return link.toLinkResponse();
    }

    @Override
    public LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest) {
        log.info("Remove link {} from chat {}", removeLinkRequest.link(), chatId);
        checkChatRegistered(chatId);
        var link = linkRepository.findByUrl(removeLinkRequest.link());
        chatLinkRepository.remove(chatId, link.id());
        if (chatLinkRepository.findAllByLinkId(link.id()).isEmpty()) { // if no chat links
            linkRepository.remove(link.id()); // remove link
            log.info("Removed link {}", removeLinkRequest.link());
        }
        log.info("Removed link {} from chat {}", removeLinkRequest.link(), chatId);
        return new LinkResponse(chatId, link.url());
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
