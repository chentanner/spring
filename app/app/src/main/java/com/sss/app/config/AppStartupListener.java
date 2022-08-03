package com.sss.app.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppStartupListener {
    private static final Logger logger = LogManager.getLogger(AppStartupListener.class);

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("--------Application started initializing managers!");
        StartUpManager startupManager = new ServerStartupManager();
        startupManager.initialize();
        startupManager.start();
    }
}
