package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.model.Chat;
import edu.java.scrapper.model.ChatLink;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
public class TgChatsServiceRepoImpl implements TgChatsService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void registerChat(Chat chat) {
        log.info("Registering chat {}", chat);
        if (chatRepository.existsById(chat.id())) {
            log.info("Chat {} already registered", chat);
            throw new ChatAlreadyRegisteredException();
        }
        chatRepository.add(chat);
        log.info("Chat {} registered", chat);
    }

    @Override
    @Transactional
    public void deleteChat(Long id) {
        log.info("Deleting chat {}", id);
        if (!chatRepository.existsById(id)) {
            log.info("Chat {} not registered", id);
            throw new ChatNotRegisteredException();
        }
        var linkIds = chatLinkRepository.findAllByChatId(id).stream().map(ChatLink::linkId).collect(Collectors.toSet());
        chatLinkRepository.removeAllByChatId(id);
        chatRepository.removeById(id);
        linkIds.forEach(linkId -> {
            if (chatLinkRepository.findAllByLinkId(linkId).isEmpty()) {
                linkRepository.remove(linkId);
            }
        });
        log.info("Chat {} deleted", id);
    }
}
