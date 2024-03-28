package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.domain.jpa.entity.ChatEntity;
import edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import edu.java.scrapper.exception.ChatAlreadyRegisteredException;
import edu.java.scrapper.exception.ChatNotRegisteredException;
import edu.java.scrapper.model.Chat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TgChatsServiceJpaImpl implements TgChatsService {

    private final JpaChatRepository jpaChatRepository;

    private final JpaLinkRepository jpaLinkRepository;


    @Override
    public void registerChat(Chat chat) {
        if (jpaChatRepository.existsById(chat.id())) {
            throw new ChatAlreadyRegisteredException();
        }
        jpaChatRepository.save(ChatEntity.fromModelChat(chat));
    }

    @Override
    public void deleteChat(Long id) {
        var chatEntity = jpaChatRepository.findById(id)
            .orElseThrow(ChatNotRegisteredException::new);
        chatEntity.getLinks()
            .forEach(linkEntity -> {
                chatEntity.removeLink(linkEntity);
                if (linkEntity.getChats().isEmpty()) {
                    jpaLinkRepository.delete(linkEntity);
                }
            });
        jpaChatRepository.delete(chatEntity);
    }
}
