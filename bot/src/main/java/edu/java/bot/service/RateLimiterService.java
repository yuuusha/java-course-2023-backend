package edu.java.bot.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final List<String> whitelist;

    public RateLimiterService(@Value("${rate-limiter.whitelist}") List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public boolean isSkipped(String ip) {
        return whitelist.contains(ip);
    }

}
