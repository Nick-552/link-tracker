package edu.java.scrapper.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface JooqTest {

    @DynamicPropertySource
    static void jooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.databaseAccessType", () -> "jooq");
    }
}
