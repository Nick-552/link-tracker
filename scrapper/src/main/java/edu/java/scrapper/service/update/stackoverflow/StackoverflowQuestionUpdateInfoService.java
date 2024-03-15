package edu.java.scrapper.service.update.stackoverflow;

import edu.java.scrapper.model.LinkType;
import edu.java.scrapper.service.fetcher.StackoverflowDataFetcher;
import edu.java.scrapper.service.update.UpdateInfo;
import edu.java.scrapper.service.update.UpdateInfoService;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackoverflowQuestionUpdateInfoService implements UpdateInfoService {

    private final StackoverflowDataFetcher stackoverflowDataFetcher;

    @Override
    public OffsetDateTime getLastUpdate(URI url) {
        return stackoverflowDataFetcher.getQuestionInfo(url).lastUpdate();
    }

    @Override
    public UpdateInfo getUpdateInformation(URI url) {
        return new UpdateInfo("Страница была обновлена", getLastUpdate(url));
    }

    @Override
    public LinkType linkType() {
        return LinkType.STACKOVERFLOW_QUESTION;
    }

}
