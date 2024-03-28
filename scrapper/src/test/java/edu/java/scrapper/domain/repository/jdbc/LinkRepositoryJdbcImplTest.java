package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.LinkRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

class LinkRepositoryJdbcImplTest
    extends LinkRepositoryTest<LinkRepositoryJdbcImpl>
    implements JdbcRepositoryTest
{

    @Autowired
    private JdbcClient jdbcClient;

    @Override
    protected LinkRepositoryJdbcImpl createInstance() {
        return new LinkRepositoryJdbcImpl(jdbcClient);
    }
}
