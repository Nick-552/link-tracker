package edu.java.scrapper.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public interface JpaTest {

    @DynamicPropertySource
    static void jpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("app.databaseAccessType", () -> "jpa");
    }
}
