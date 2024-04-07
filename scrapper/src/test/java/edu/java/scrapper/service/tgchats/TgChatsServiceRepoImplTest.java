package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.util.JdbcTest;
import org.springframework.beans.factory.annotation.Autowired;


class TgChatsServiceRepoImplTest
    extends TgChatsServiceTest<TgChatsServiceRepoImpl>
    implements JdbcTest
{

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    ChatLinkRepository chatLinkRepository;

    @Override
    protected TgChatsServiceRepoImpl createInstance() {
        return new TgChatsServiceRepoImpl(
            linkRepository,
            chatLinkRepository,
            chatRepository
        );
    }
}
