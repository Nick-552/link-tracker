package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.LinkRepositoryTest;
import edu.java.scrapper.util.JooqTest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

class LinkRepositoryJooqImplTest
    extends LinkRepositoryTest<LinkRepositoryJooqImpl>
    implements JooqTest
{

    @Autowired
    private DSLContext dslContext;

    @Override
    protected LinkRepositoryJooqImpl createInstance() {
        return new LinkRepositoryJooqImpl(dslContext);
    }
}
