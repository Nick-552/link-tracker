package edu.java.scrapper.service.links;

import edu.java.scrapper.util.JdbcTest;
import org.springframework.beans.factory.annotation.Autowired;

class LinksServiceRepoImplTest
    extends LinksServiceTest<LinksServiceRepoImpl>
    implements JdbcTest
{

    @Autowired
    LinksServiceRepoImpl linkService;

    @Override
    protected LinksServiceRepoImpl createInstance() {
        return linkService;
    }
}
