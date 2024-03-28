package edu.java.scrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "BOT_API_CLIENT_BASE_URL=http://localhost:8090"
})
public class ScrapperApplicationTest {

    @Test
    @DisplayName("Spring context loading & beans initialization test")
    void test() {
    }
}
