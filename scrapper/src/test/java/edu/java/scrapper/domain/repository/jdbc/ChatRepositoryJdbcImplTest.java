package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.ChatRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

class ChatRepositoryJdbcImplTest
    extends ChatRepositoryTest<ChatRepositoryJdbcImpl>
    implements JdbcRepositoryTest
{

    @Autowired
    private JdbcClient jdbcClient;

    @Override
    protected ChatRepositoryJdbcImpl createInstance() {
        return new ChatRepositoryJdbcImpl(jdbcClient);
    }
}
