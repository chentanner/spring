package com.sss.app.core.entity.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public class ApplicationContextFactory {
    private static final Logger logger = LogManager.getLogger();
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

    public static boolean hasApplicationContext() {
        return getApplicationContext() != null;
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

    public static EntityManager getCurrentEntityManagerOnThread() {
        EntityManagerHolder entityManagerHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(
                getEntityManagerFactory());
        if (entityManagerHolder != null)
            return entityManagerHolder.getEntityManager();
        return null;
    }

    public static void closeAndRemoveEntityManagerOnThread() {
        EntityManagerHolder holder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(
                getEntityManagerFactory());
        if (holder == null) {
            return;
        }

        if (holder.getEntityManager().getTransaction().isActive()) {
            logger.error("Current transaction is active on a thread");
            logger.error("Transaction name: " + TransactionSynchronizationManager.getCurrentTransactionName());
            holder.getEntityManager().getTransaction().rollback();
        }

        try {
            logger.error("Terminating session on acquired thread.");
            holder.getEntityManager().close();
        } catch (PersistenceException pe) {
            logger.error("Could not close JPA entityManager" + pe.getMessage());
        } catch (RuntimeException re) {
            logger.error("Unexpected exception on cosing JPA EntityManager" + re);
        } finally {
            TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
        }
    }

    public static void shutDown() {
        ConfigurableApplicationContext cac = (ConfigurableApplicationContext) getApplicationContext();
        cac.stop();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static SessionFactory getSessionFactory() {
        return (SessionFactoryImplementor) getEntityManagerFactory();
    }

    public static void flushIfInSession() {
//        EntityManager entityManager = getcurr
    }
}
