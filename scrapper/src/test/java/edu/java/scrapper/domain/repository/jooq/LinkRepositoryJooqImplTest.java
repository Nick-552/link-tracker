package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.LinkRepositoryTest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

class LinkRepositoryJooqImplTest
    extends LinkRepositoryTest<LinkRepositoryJooqImpl>
    implements JooqRepositoryTest
{

    @Autowired
    private DSLContext dslContext;

    @Override
    protected LinkRepositoryJooqImpl createInstance() {
        return new LinkRepositoryJooqImpl(dslContext);
    }
}
