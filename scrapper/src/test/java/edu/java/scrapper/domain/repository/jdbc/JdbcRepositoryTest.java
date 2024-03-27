package edu.java.scrapper.domain.repository.jdbc;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface JdbcRepositoryTest {

    @DynamicPropertySource
    static void jdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.databaseAccessType", () -> "jdbc");
    }
}
