package edu.java.scrapper.service.update.stackoverflow;

import edu.java.scrapper.service.fetcher.StackoverflowDataFetcher;
import edu.java.scrapper.service.update.UpdateInfo;
import edu.java.scrapper.service.update.UpdateInfoService;
import lombok.RequiredArgsConstructor;
import edu.java.scrapper.model.LinkType;
import java.net.URI;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class StackoverflowQuestionUpdateInfoService implements UpdateInfoService {

    private final StackoverflowDataFetcher stackoverflowDataFetcher;

    @Override
    public OffsetDateTime getLastUpdate(URI url) {
        return stackoverflowDataFetcher.getQuestionInfo(url).lastUpdate();
    }

    @Override
    public UpdateInfo getUpdateInformation(URI url) {
        return null;
    }

    @Override
    public LinkType linkType() {
        return LinkType.STACKOVERFLOW_QUESTION;
    }

}
