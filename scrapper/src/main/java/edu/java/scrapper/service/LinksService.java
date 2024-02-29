package edu.java.scrapper.service;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.LinksListResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LinksService {

    public static LinkResponse createLinkResponseWithId(Long id) {
        return new LinkResponse(id, URI.create("https://github.com/Nick-552/link-tracker"));
    }

    public LinksListResponse getLinks(Long chatId) {
        log.info(chatId);
        return new LinksListResponse(
            List.of(
                createLinkResponseWithId(chatId)
            ), 1
        );
    }

    public LinkResponse addLink(Long chatId, @Valid AddLinkRequest addLinkRequest) {
        log.info(addLinkRequest);
        return createLinkResponseWithId(chatId);
    }

    public LinkResponse removeLink(Long chatId, @Valid RemoveLinkRequest removeLinkRequest) {
        log.info(removeLinkRequest);
        return createLinkResponseWithId(chatId);
    }
}
