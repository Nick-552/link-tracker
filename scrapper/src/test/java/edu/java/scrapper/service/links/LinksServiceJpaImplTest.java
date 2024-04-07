package edu.java.scrapper.service.links;

import edu.java.scrapper.util.JpaTest;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

class LinksServiceJpaImplTest
    extends LinksServiceTest<LinksServiceJpaImpl>
    implements JpaTest
{

    @Autowired
    private EntityManager manager;

    @Autowired
    private LinksServiceJpaImpl linksService;

    @Override
    void flush() {
        manager.flush();
    }

    @Override
    protected LinksServiceJpaImpl createInstance() {
        return linksService;
    }
}
