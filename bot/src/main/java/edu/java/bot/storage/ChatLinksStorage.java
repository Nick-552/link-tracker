package edu.java.bot.storage;

import edu.java.bot.dto.request.scrapper.AddLinkRequest;
import edu.java.bot.dto.request.scrapper.RemoveLinkRequest;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import edu.java.bot.dto.response.scrapper.LinksListResponse;
import org.springframework.stereotype.Service;

@Service
public interface ChatLinksStorage {

    Void addTgChat(Long chatId);

    Void deleteTgChat(Long chatId);

    LinksListResponse getLinks(Long chatId);

    LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest);

    LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest);
}
