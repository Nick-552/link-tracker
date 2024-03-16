package edu.java.scrapper.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Log4j2
public class TgChatsService {

    public void registerChat(@PathVariable Long id) {
        log.info(id);
    }

    public void deleteChat(@PathVariable Long id) {
        log.info(id);
    }
}
