package com.sss.app.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerStartupManager implements StartUpManager {
    private static final Logger logger = LogManager.getLogger(StartUpManager.class);

    @Override
    public void initialize() {
        // Initialize singleton beans if needed
    }

    @Override
    public void start() {
        logger.info("---------------Application starting up ");

    }
}
