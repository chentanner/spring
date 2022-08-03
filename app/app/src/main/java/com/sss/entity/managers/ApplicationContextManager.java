package com.sss.entity.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component()
public class ApplicationContextManager implements ApplicationContextAware {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextFactory.setContextFromWeb(applicationContext);
        logger.info("ApplicationContext Set");
        ApplicationContextFactory.printBeans();

    }
}
