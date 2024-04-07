package edu.java.scrapper.service.tgchats;

import edu.java.scrapper.domain.jpa.repository.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repository.JpaLinkRepository;
import edu.java.scrapper.util.JpaTest;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

class TgChatsServiceJpaImplTest
    extends TgChatsServiceTest<TgChatsServiceJpaImpl>
    implements JpaTest
{

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private EntityManager manager;

    @Override
    protected TgChatsServiceJpaImpl createInstance() {
        return new TgChatsServiceJpaImpl(
            jpaChatRepository,
            jpaLinkRepository
        );
    }

    @Override
    public void flush() {
        manager.flush();
    }
}
