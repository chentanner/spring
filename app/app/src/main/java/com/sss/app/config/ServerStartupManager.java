package com.sss.app.config;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.error.manager.ErrorMessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerStartupManager implements StartUpManager {
    private static final Logger logger = LogManager.getLogger(StartUpManager.class);

    @Override
    public void initialize() {
        // Initialize singleton beans if needed
        ErrorMessageManager errorMessageManager = ApplicationContextFactory.getBean(ErrorMessageManager.class);
        errorMessageManager.initialize();
    }

    @Override
    public void start() {
        logger.info("---------------Application starting up ");

    }
}
