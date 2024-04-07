package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.ChatLinkRepositoryTest;
import edu.java.scrapper.util.JdbcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

class ChatLinkRepositoryJdbcImplTest
    extends ChatLinkRepositoryTest<ChatLinkRepositoryJdbcImpl>
    implements JdbcTest
{

    @Autowired
    private JdbcClient jdbcClient;

    @Override
    protected ChatLinkRepositoryJdbcImpl createInstance() {
        return new ChatLinkRepositoryJdbcImpl(jdbcClient);
    }
}
