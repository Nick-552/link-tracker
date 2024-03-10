package edu.java.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "SCRAPPER_API_CLIENT_BASE_URL=http://localhost:8080"
})
public class BotApplicationTest {

    @DisplayName("Spring context loading & bean initialization test")
    @Test
    void test() {

    }
}
