package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatLinkRepositoryTest;
import edu.java.scrapper.util.JooqTest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

class ChatLinkRepositoryJooqImplTest
    extends ChatLinkRepositoryTest<ChatLinkRepositoryJooqImpl>
    implements JooqTest
{

    @Autowired
    private DSLContext dslContext;

    @Override
    protected ChatLinkRepositoryJooqImpl createInstance() {
        return new ChatLinkRepositoryJooqImpl(dslContext);
    }
}
