package edu.java.scrapper.domain.repository.jooq;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface JooqRepositoryTest {

    @DynamicPropertySource
    static void jooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.databaseAccessType", () -> "jooq");
    }
}
