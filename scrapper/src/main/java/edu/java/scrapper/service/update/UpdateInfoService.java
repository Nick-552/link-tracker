package edu.java.scrapper.service.update;

import edu.java.scrapper.model.LinkType;
import java.net.URI;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;

public interface UpdateInfoService {

    OffsetDateTime getLastUpdate(URI url);

    UpdateInfo getUpdateInformation(URI url);

    LinkType linkType();

    @Autowired
    default void init(UpdateInfoServiceProvider provider) {
        provider.register(linkType(), this);
    }
}
