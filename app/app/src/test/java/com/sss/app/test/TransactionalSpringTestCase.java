package com.sss.app.test;

import com.sss.app.core.auth.service.UserAuthorizationManager;
import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.entity.managers.StatelessSessionManager;
import com.sss.app.core.entity.model.AuditManager;
import com.sss.app.settings.cache.ApplicationSettingCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-int.properties")
public abstract class TransactionalSpringTestCase extends BaseTestCase {

    private static final Logger logger = LogManager.getLogger();

    protected EntityManagerFactory masterEntityMgrFactory;
    protected TransactionStatus transactionStatus;
    private boolean commit = false;

    @BeforeEach
    public void beforeRun() throws Throwable {
        initiateSession();
        setUpTransaction();
        addManagers();
        setUp();
    }

    @AfterEach
    public void afterRun() throws Throwable {
        tearDown();
        JpaTransactionManager transMgr = getBean(JpaTransactionManager.class);
        if (!transactionStatus.isCompleted()) {
            if (commit)
                transMgr.commit(transactionStatus);
            else
                transMgr.rollback(transactionStatus);
        }

        terminateSession();
    }

    protected void initiateSession() {
        masterEntityMgrFactory = getBean(EntityManagerFactory.class);
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(
                masterEntityMgrFactory);
        if (emHolder != null) {
            emHolder.getEntityManager();
            logger.debug("Session already exists. Not managed by me.");
            return;
        }

        logger.trace("Creating new session. Managed by TransactionalSpringTestCase.");
        EntityManager entityManager = masterEntityMgrFactory.createEntityManager();
        TransactionSynchronizationManager.bindResource(masterEntityMgrFactory, new EntityManagerHolder(entityManager));
    }

    private void setUpTransaction() {
        JpaTransactionManager transMgr = getBean(JpaTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("master");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        if (masterEntityMgrFactory != transMgr.getEntityManagerFactory())
            logger.debug("entity mgrs from session and transMgr not the same");

        masterEntityMgrFactory = transMgr.getEntityManagerFactory();
        transactionStatus = transMgr.getTransaction(def);

        logger.trace("clearing conversionMgr and cache manager");
        AuditManager userManager = getBean(AuditManager.class);
        userManager.setCurrentAuditUserName("test");
        flush();

        ApplicationSettingCacheManager applicationSettingCacheManager = getBean(
                ApplicationSettingCacheManager.class);
        applicationSettingCacheManager.initialize();

        StatelessSessionManager sessionManager = getBean(StatelessSessionManager.class);
        sessionManager.setTest(true);

        UserAuthorizationManager userAuthorizationManagerMock = getBean(
                UserAuthorizationManager.class);
        userAuthorizationManagerMock.initialize();

        flush();
    }

    protected void addManagers() {

        flush();
    }

    abstract public void setUp();


    protected void tearDown() throws Exception {
    }

    protected void terminateSession() {
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(
                masterEntityMgrFactory);
        if (emHolder == null) {
            logger.trace("No session to terminate");
            return;
        } else {
            emHolder.getEntityManager();
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            logger.trace("Current transaction is active.");
        }

        logger.trace("Terminating session created by TransactionalSpringTestCase");
        try {
            emHolder.getEntityManager().close();
        } catch (PersistenceException ex) {
            logger.trace("Could not close JPA EntityManager" + ex.getMessage());
        } catch (RuntimeException ex) {
            logger.trace("Unexpected exception on closing JPA EntityManager" + ex);
        } finally {
            TransactionSynchronizationManager.unbindResource(masterEntityMgrFactory);
        }
    }

    public void commit() {
        JpaTransactionManager transMgr = getBean(JpaTransactionManager.class);
        transMgr.commit(transactionStatus);
    }

    public void rollback() {
        JpaTransactionManager transMgr = getBean(JpaTransactionManager.class);
        transMgr.rollback(transactionStatus);
    }

    public void flush() {
        EntityManager masterEntityMgr = EntityManagerFactoryUtils.getTransactionalEntityManager(masterEntityMgrFactory);
        masterEntityMgr.flush();
    }

    protected <T> T getBean(Class<T> clazz) {
        return ApplicationContextFactory.getBean(clazz);
    }
}
