package edu.java.scrapper.service.links;

import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import edu.java.scrapper.domain.jpa.entity.LinkEntity;
import edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.service.update.UpdateInfoServiceProvider;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

public class LinksServiceJpaImpl extends LinksService {

    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    public LinksServiceJpaImpl(
        UpdateInfoServiceProvider updateInfoServiceProvider,
        JpaChatRepository jpaChatRepository,
        JpaLinkRepository jpaLinkRepository
    ) {
        super(updateInfoServiceProvider);
        this.jpaChatRepository = jpaChatRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    @Transactional
    public LinksListResponse getLinksForChatId(Long chatId) {
        var chatEntity = jpaChatRepository.findById(chatId)
            .orElseThrow(ChatNotRegisteredException::new); // chat not registered
        var linkEntities = chatEntity.getLinks();
        var links = linkEntities.stream()
            .map(LinkEntity::toModelLink)
            .map(Link::toLinkResponse)
            .toList();
        return new LinksListResponse(links, links.size());
    }

    @Override
    @Transactional
    public LinkResponse addLinkToChat(Long chatId, AddLinkRequest addLinkRequest) {
        var chatEntity = jpaChatRepository.findById(chatId)
            .orElseThrow(ChatNotRegisteredException::new); // chat not registered
        var url = addLinkRequest.link();
        var optionalLinkEntity = jpaLinkRepository.findByUrl(url.toString());
        LinkEntity linkEntity;
        if (optionalLinkEntity.isPresent()) {
            linkEntity = optionalLinkEntity.get();
            if (chatEntity.getLinks().contains(linkEntity)) {
                throw new LinkAlreadyTrackedException();
            }
        } else {
            var lastUpdate = updateInfoServiceProvider.provide(url).getLastUpdate(url);
            linkEntity = new LinkEntity(
                url.toString(),
                lastUpdate,
                OffsetDateTime.now()
            );
            jpaLinkRepository.save(linkEntity);
        }
        chatEntity.addLink(linkEntity);
        return new LinkResponse(linkEntity.toModelLink().id(), url);
    }

    @Override
    @Transactional
    public LinkResponse removeLinkFromChat(Long chatId, RemoveLinkRequest removeLinkRequest) {
        var url = removeLinkRequest.link();
        var linkEntity = jpaLinkRepository.findByUrl(url.toString())
            .orElseThrow(LinkNotFoundException::new); // no such link in links
        var chatEntity = jpaChatRepository.findById(chatId)
            .orElseThrow(ChatNotRegisteredException::new); // chat not registered
        chatEntity.removeLink(linkEntity);
        if (linkEntity.getChats().isEmpty()) {
            jpaLinkRepository.delete(linkEntity); // remove link
        }
        return new LinkResponse(chatId, url);
    }

    @Override
    public void updateLink(Link link) {
        var linkEntity = jpaLinkRepository.findById(link.id())
            .orElseThrow(LinkNotFoundException::new); // no such link in links
        linkEntity.setLastUpdatedAt(link.lastUpdatedAt());
        linkEntity.setLastCheckedAt(link.lastCheckAt());
    }

    @Override
    public List<Long> getChatIdsForLink(Link link) {
        var linkEntity = jpaLinkRepository.findById(link.id())
            .orElseThrow(LinkNotFoundException::new); // no such link in links
        return linkEntity.getChats()
            .stream()
            .map(ChatEntity::getId)
            .toList();
    }

    @Override
    public List<Link> listStaleLinks(int limit, Duration minTimeSinceLastCheck) {
        return jpaLinkRepository
            .findAllByLastCheckedAtIsBeforeOrderByLastCheckedAt(
                OffsetDateTime.now().minus(minTimeSinceLastCheck),
                Limit.of(limit)
            ).stream()
            .map(LinkEntity::toModelLink)
            .toList();
    }
}
