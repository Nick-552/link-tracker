package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.model.Chat;
import edu.java.scrapper.model.ChatLink;
import edu.java.scrapper.repository.jdbc.ChatLinkRepository;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.repository.jdbc.LinkRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class TgChatsServiceJdbcImpl implements TgChatsService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final ChatRepository chatRepository;

    @Override
    public void registerChat(Long id) {
        log.info("Registering chat {}", id);
        try {
            chatRepository.add(new Chat(id));
            log.info("Chat {} registered", id);
        } catch (DataIntegrityViolationException e) {
            log.info("Chat {} already registered", id);
            throw new ChatAlreadyRegisteredException();
        }
    }

    @Override
    public void deleteChat(Long id) {
        log.info("Deleting chat {}", id);
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
