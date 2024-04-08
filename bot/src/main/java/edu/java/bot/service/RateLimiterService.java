package edu.java.bot.service;

import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final List<String> ipWhitelist;

    public RateLimiterService(ApplicationConfig applicationConfig) {
        this.ipWhitelist = applicationConfig.ipWhitelist();
    }

    public boolean isSkipped(String ip) {
        return ipWhitelist.contains(ip);
    }
}
