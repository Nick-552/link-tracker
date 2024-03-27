package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatRepositoryTest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

class ChatRepositoryJooqImplTest
    extends ChatRepositoryTest<ChatRepositoryJooqImpl>
    implements JooqRepositoryTest
{

    @Autowired
    private DSLContext dslContext;

    @Override
    protected ChatRepositoryJooqImpl createInstance() {
        return new ChatRepositoryJooqImpl(dslContext);
    }
}
