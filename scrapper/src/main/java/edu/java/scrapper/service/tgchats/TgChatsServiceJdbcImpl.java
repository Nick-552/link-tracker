package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.repository.jdbc.ChatLinkRepository;
import edu.java.scrapper.repository.jdbc.ChatRepository;
import edu.java.scrapper.repository.jdbc.LinkRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import edu.java.scrapper.model.Chat;
import edu.java.scrapper.model.ChatLink;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TgChatsServiceJdbcImpl implements TgChatsService {

    private final LinkRepository linkRepository;

    private final ChatLinkRepository chatLinkRepository;

    private final ChatRepository chatRepository;

    @Override
    public void registerChat(Long id) {
        try {
            chatRepository.add(new Chat(id));
        } catch (DataIntegrityViolationException e) {
            throw new ChatAlreadyRegisteredException();
        }
    }

    @Override
    public void deleteChat(Long id) {
        var linkIds = chatLinkRepository.findAllByChatId(id).stream().map(ChatLink::linkId).collect(Collectors.toSet());
        chatLinkRepository.removeAllByChatId(id);
        chatRepository.removeById(id);
        linkIds.forEach(linkId -> {
            if (chatLinkRepository.findAllByLinkId(linkId).isEmpty()) {
                linkRepository.remove(linkId);
            }
        });
    }
}
