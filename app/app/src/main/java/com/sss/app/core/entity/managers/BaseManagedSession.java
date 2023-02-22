package com.sss.app.core.entity.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

/**
 * Provides support for a programmer managed sessions and transactions. Note that this class will create a session and transaction on
 * the thread using ThreadLocal.
 */
public class BaseManagedSession {


    private static final Logger logger = LogManager.getLogger(BaseManagedSession.class);

    private static final ThreadLocal<ManagedSessionThreadLocal> sessionHolder = new ThreadLocal<ManagedSessionThreadLocal>() {
        protected synchronized ManagedSessionThreadLocal initialValue() {
            return new ManagedSessionThreadLocal();
        }
    };

    /**
     * This method will bind an entityManager to the thread if there is no entityManager previously on the thread.
     */
    protected void initiateSession() {
        EntityManagerFactory factory = ApplicationContextFactory.getBean(EntityManagerFactory.class);
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(factory);
        if (emHolder != null) {
            emHolder.getEntityManager();
            setManagedByMe(false);
            return;
        }

        setManagedByMe(true);
        EntityManager entityManager = factory.createEntityManager();
        TransactionSynchronizationManager.bindResource(factory, new EntityManagerHolder(entityManager));
    }

    /**
     * this method will successfully terminate a JPA entity manager on the thread.
     */
    protected void terminateSession() {
        EntityManagerFactory factory = ApplicationContextFactory.getBean(EntityManagerFactory.class);
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(factory);
        if (emHolder == null) {
            return;
        } else {
            emHolder.getEntityManager();
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            logger.error("Current transaction is active.");
        }

        if (isManagedByMe()) {
            if (!hasCurrentTransaction())
                return;

            if (!getTransactionStatus().isCompleted()) {
                logger.error("rolling back transaction that was not commited by session termination" +
                                     getTransactionStatus().toString());
                rollbackTransaction();
            }

            try {

                emHolder.getEntityManager().close();

            } catch (PersistenceException ex) {
                logger.error("Could not close JPA EntityManager" + ex.getMessage());
            } catch (RuntimeException ex) {
                logger.error("Unexpected exception on closing JPA EntityManager" + ex);
            } finally {
                TransactionSynchronizationManager.unbindResource(factory);
            }

        }
    }

    protected void beginTransaction() {
        EntityManagerFactory factory = ApplicationContextFactory.getBean(EntityManagerFactory.class);
        if (!isManagedByMe())
            return;

        JpaTransactionManager transMgr = ApplicationContextFactory.getBean(JpaTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("master");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        if (factory != transMgr.getEntityManagerFactory())
            logger.warn("entity mgrs from session and transMgr not the same");

        factory = transMgr.getEntityManagerFactory();
        setTransactionStatus(transMgr.getTransaction(def));
    }

    protected void commitTransaction() {
        if (!isManagedByMe())
            return;
        if (!hasCurrentTransaction())
            return;

        JpaTransactionManager transMgr = ApplicationContextFactory.getBean(JpaTransactionManager.class);
        if (getTransactionStatus().isRollbackOnly())
            transMgr.rollback(getTransactionStatus());
        else
            transMgr.commit(getTransactionStatus());
    }

    protected void rollbackTransaction() {
        if (!isManagedByMe())
            return;
        if (!hasCurrentTransaction())
            return;

        if (!getTransactionStatus().isCompleted()) {
            JpaTransactionManager transMgr = ApplicationContextFactory.getBean(JpaTransactionManager.class);
            transMgr.rollback(getTransactionStatus());
        }
    }

    private boolean hasCurrentTransaction() {
        ManagedSessionThreadLocal sessionTL = getManagedSessionThreadLocal();
        return sessionTL.getTransactionStatus() != null;
    }

    private TransactionStatus getTransactionStatus() {
        ManagedSessionThreadLocal sessionTL = getManagedSessionThreadLocal();
        return sessionTL.getTransactionStatus();
    }

    private void setTransactionStatus(TransactionStatus transactionStatus) {
        ManagedSessionThreadLocal sessionTL = getManagedSessionThreadLocal();
        sessionTL.setTransactionStatus(transactionStatus);
    }

    public boolean isManagedByMe() {
        ManagedSessionThreadLocal sessionTL = getManagedSessionThreadLocal();
        return sessionTL.isManagedByMe();
    }

    public void setManagedByMe(boolean managed) {
        ManagedSessionThreadLocal sessionTL = getManagedSessionThreadLocal();
        sessionTL.setManagedByMe(managed);
    }

    private ManagedSessionThreadLocal getManagedSessionThreadLocal() {
        ManagedSessionThreadLocal sessionTl = sessionHolder.get();
        if (sessionTl == null)
            sessionHolder.set(new ManagedSessionThreadLocal());

        return sessionHolder.get();
    }

    protected boolean shouldFlush() {
        return (hasCurrentTransaction() && !getTransactionStatus().isCompleted());
    }

    public EntityManager getSession() {
        EntityManagerFactory factory = ApplicationContextFactory.getBean(EntityManagerFactory.class);
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(factory);
        if (emHolder == null) {
            return null;
        } else {
            emHolder.getEntityManager();
        }
        return emHolder.getEntityManager();


    }

    protected void flushSession() {
        EntityManagerFactory factory = ApplicationContextFactory.getBean(EntityManagerFactory.class);
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(factory);
        if (emHolder == null) {
            return;
        } else {
            emHolder.getEntityManager();
        }

        if (isManagedByMe()) {

            try {
                emHolder.getEntityManager().flush();
                // catch this exception has it is safe to ignore.
            } catch (PersistenceException ex) {
                logger.debug("Could not flush JPA EntityManager" + ex.getMessage());
            } catch (RuntimeException ex) {
                logger.debug("Unexpected exception on flushing JPA EntityManager" + ex);
            }
        }
    }

    protected void clearSession() {
        EntityManagerFactory factory = ApplicationContextFactory.getBean(EntityManagerFactory.class);
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(factory);
        if (emHolder == null) {
            return;
        } else {
            emHolder.getEntityManager();
        }

        if (isManagedByMe()) {

            try {

                emHolder.getEntityManager().clear();

            } catch (PersistenceException ex) {
                logger.error("Could not clear JPA EntityManager" + ex.getMessage());
            } catch (RuntimeException ex) {
                logger.error("Unexpected exception on clearing JPA EntityManager" + ex);
            }
        }
    }

    private static class ManagedSessionThreadLocal {
        private TransactionStatus transactionStatus;
        private boolean isManagedByMe = false;
        private boolean isBatch = false;

        public TransactionStatus getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(TransactionStatus transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public boolean isManagedByMe() {
            return isManagedByMe;
        }

        public void setManagedByMe(boolean isManagedByMe) {
            this.isManagedByMe = isManagedByMe;
        }

        public boolean isBatch() {
            return isBatch;
        }

        public void setBatch(boolean isBatch) {
            this.isBatch = isBatch;
        }
    }
}
