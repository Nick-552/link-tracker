package edu.java.scrapper.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface JdbcTest {

    @DynamicPropertySource
    static void jdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.databaseAccessType", () -> "jdbc");
    }
}
