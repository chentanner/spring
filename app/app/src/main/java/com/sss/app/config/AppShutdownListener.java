package com.sss.app.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LogManager.getLogger(AppShutdownListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("--------Application shutdown at " + LocalDateTime.now());
    }
}
