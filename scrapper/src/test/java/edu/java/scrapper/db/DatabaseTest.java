package edu.java.scrapper.db;

import edu.java.scrapper.IntegrationEnvironment;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseTest extends IntegrationEnvironment {

    @Test
    @SneakyThrows
    void test() {
        var connection = POSTGRES.createConnection("");
        var chats = connection.prepareStatement("SELECT * FROM chats").getMetaData();
        var links = connection.prepareStatement("SELECT * FROM links").getMetaData();
        var chats_links = connection.prepareStatement("SELECT * FROM chats_links").getMetaData();
        assertThat(chats.getColumnCount()).isGreaterThanOrEqualTo(1);
        assertThat(links.getColumnCount()).isGreaterThanOrEqualTo(4);
        assertThat(chats_links.getColumnCount()).isGreaterThanOrEqualTo(3);
    }
}
