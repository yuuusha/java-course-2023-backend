package edu.java.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "${app.scheduler.delay}")
    public void update() {
        log.info("update...");
    }

}
