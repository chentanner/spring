package com.sss.app.core.entity.managers;

import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component(value = "statelessSessionManager")
public class StatelessSessionManager {

    public static final String BEAN_NAME = "statelessSessionManager";

    private static final Logger logger = LogManager.getLogger(StatelessSessionManager.class);

    private static ThreadLocal<StatelessSessionHolder> threadLocalSessionHolder = new ThreadLocal<>() {
        protected synchronized StatelessSessionHolder initialValue() {
            return new StatelessSessionHolder();
        }
    };

    private boolean isTest = false;

    public StatelessSessionManager() {
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean isTest) {
        this.isTest = isTest;
    }

    public boolean hasStatelessSession() {
        if (isTest)
            return true;

        if (threadLocalSessionHolder.get() == null)
            return false;
        StatelessSession session = threadLocalSessionHolder.get().getSession();
        return session != null;
    }

    public void startNewSession() {
        if (isTest)
            return;
        if (threadLocalSessionHolder.get() != null) {
            StatelessSession session = threadLocalSessionHolder.get().getSession();
            if (session != null)
                throw new ApplicationRuntimeException(TransactionErrorCode.STATELESS_SESSION_CREATE_FAILED_EXISTS.getCode());
        }

        StatelessSession session = createSession();

        logger.trace("Starting new session " + session.toString() + " Transaction:" + session.getTransaction().toString() + " on thread: " + Thread.currentThread().getName());


    }

    public StatelessSession getSession() {
        if (threadLocalSessionHolder.get().getSession() == null) {
            return null;
        }

        StatelessSession session = threadLocalSessionHolder.get().getSession();

        logger.trace("getting session " + session.toString() + " Transaction:" + session.getTransaction().toString() + " on thread: " + Thread.currentThread().getName());
        return session;
    }

    private StatelessSession createSession() {
        SessionFactory sessionFactory = ApplicationContextFactory.getSessionFactory();
        StatelessSession session = sessionFactory.openStatelessSession();
        threadLocalSessionHolder.get().setSession(session);
        return session;
    }

    public void save(Object entity) {
        StatelessSession session = getSession();
        if (session == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.HIBERNATE_SESSION.getCode());
        session.insert(entity);
    }

    public void rollback() {

        StatelessSession session = getSession();
        if (session == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.HIBERNATE_SESSION.getCode());
        session.getTransaction().rollback();
    }

    public void close() {
        if (isTest)
            return;
        StatelessSession session = getSession();
        if (session == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.HIBERNATE_SESSION.getCode());
        logger.trace("Closing new session " + session.toString() + " Transaction:" + session.getTransaction().toString() + " on thread: " + Thread.currentThread().getName());
        session.close();
        threadLocalSessionHolder.get().setSession(null);
    }

    public void commit() {
        if (isTest)
            return;
        StatelessSession session = getSession();
        if (session == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.HIBERNATE_SESSION.getCode());
        session.getTransaction().commit();
    }

    private static class StatelessSessionHolder {
        private StatelessSession session;
        private Transaction transaction;

        public StatelessSessionHolder() {
        }

        public StatelessSession getSession() {
            return session;
        }

        public void setSession(StatelessSession session) {
            this.session = session;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public void setTransaction(Transaction transaction) {
            this.transaction = transaction;
        }

    }
}
