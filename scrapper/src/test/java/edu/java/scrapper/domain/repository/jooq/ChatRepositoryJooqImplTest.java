package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatRepositoryTest;
import edu.java.scrapper.util.JooqTest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

class ChatRepositoryJooqImplTest
    extends ChatRepositoryTest<ChatRepositoryJooqImpl>
    implements JooqTest
{

    @Autowired
    private DSLContext dslContext;

    @Override
    protected ChatRepositoryJooqImpl createInstance() {
        return new ChatRepositoryJooqImpl(dslContext);
    }
}
