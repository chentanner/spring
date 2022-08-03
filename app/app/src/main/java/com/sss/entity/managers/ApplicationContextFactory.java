package com.sss.entity.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.persistence.EntityManagerFactory;

public class ApplicationContextFactory {
    private static Logger logger = LogManager.getLogger(ApplicationContextFactory.class);
    private static ApplicationContext applicationContext;
    private static boolean isTest = false;

    public static synchronized void setContextFromWeb(ApplicationContext webContext) {
        applicationContext = webContext;
        isTest = false;
    }

    public static synchronized void useTest() {
        isTest = true;
    }

    public static <T> T getBean(Class<T> claz) {
        return getApplicationContext().getBean(claz);
    }

    public static <T> T getBean(String name, Class<T> claz) {
        return getApplicationContext().getBean(name, claz);
    }

    public static void printBeans() {
        if (logger.isDebugEnabled()) {
            for (String beanName : applicationContext.getBeanDefinitionNames()) {
                logger.debug(beanName);
            }
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return getBean(EntityManagerFactory.class);
    }

    public static void shutDown() {
        ConfigurableApplicationContext cac = (ConfigurableApplicationContext) getApplicationContext();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
