package edu.java.scrapper.service.update;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.time.OffsetDateTime;
import edu.java.scrapper.model.LinkType;

public interface UpdateInfoService {

    OffsetDateTime getLastUpdate(URI url);

    UpdateInfo getUpdateInformation(URI url);

    LinkType linkType();

    @PostConstruct
    default void init(UpdateInfoServiceProvider provider) {
        provider.register(linkType(), this);
    }
}
