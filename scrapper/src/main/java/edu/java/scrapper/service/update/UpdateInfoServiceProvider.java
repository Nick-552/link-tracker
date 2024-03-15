package edu.java.scrapper.service.update;

import edu.java.scrapper.exception.UnsupportedUrlException;
import edu.java.scrapper.util.LinkResolver;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import edu.java.scrapper.model.LinkType;
import org.springframework.stereotype.Service;

@Service
public class UpdateInfoServiceProvider {

    private static final Map<LinkType, UpdateInfoService> LINK_UPDATE_INFORMATION_SERVICES = new HashMap<>();

    public UpdateInfoService provide(URI url) {
        LinkType linkType = LinkResolver.getLinkType(url);
        var updateInformationService = LINK_UPDATE_INFORMATION_SERVICES.get(linkType);
        if (updateInformationService == null) {
            throw new UnsupportedUrlException();
        }
        return updateInformationService;
    }

    public void register(LinkType linkType, UpdateInfoService updateInfoService) {
        LINK_UPDATE_INFORMATION_SERVICES.put(linkType, updateInfoService);
    }
}
