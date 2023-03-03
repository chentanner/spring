package com.sss.app.core.entity.model;

import com.sss.app.core.entity.snapshot.BusinessKey;
import com.sss.app.core.entity.snapshot.IEntitySnapshot;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.exception.ApplicationValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@MappedSuperclass
public abstract class TemporalAbstractEntity extends AbstractEntity implements VersionedEntity, Validatable {
    private static Logger logger = LogManager.getLogger(TemporalAbstractEntity.class);

    private Boolean isExpired = Boolean.FALSE;
    private Long version;

    private AuditAbstractEntity recentHistory;
    private AuditAbstractEntity newHistory;

    @Transient
    protected boolean ignoreExpiredOnCreate = false; // Set only when an entity is created with expired

    @Transient
    public boolean isIgnoreExpiredOnCreate() {
        return ignoreExpiredOnCreate;
    }

    @Version
    @Column(name = "OPTIMISTIC_LOCK_NO")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void createWith(IEntitySnapshot snapshot) {
//        if (snapshot.getEntityMode() != null) {
//            if (snapshot.getEntityMode() == EntityMode.EXPIRED) {
//                isExpired = Boolean.TRUE;
//                ignoreExpiredOnCreate = true;
//            }
//        }
    }

    public void updateWith(IEntitySnapshot snapshot) {

        if (isExpired) {
            throw new ApplicationRuntimeException(TransactionErrorCode.ILLEGAL_UPDATE_EXPIRED_ENTITY.getCode());
        }

        if (snapshot.isVersioned() && this.getVersion() != null) {
            if (this.getVersion() != snapshot.getVersion()) {
                logger.error("Entity version: " + this.getVersion() + " is not equal to update version: " + snapshot.getVersion());
                throw new ApplicationRuntimeException(TransactionErrorCode.STALE_UPDATE_VERSION_FAIL.getCode());
            }
        }
    }

    @Transient
    public BusinessKey getBusinessKey() {
        if (getId() == null) {
            return null;
        } else {
            return new BusinessKey(getId());
        }
    }

    @Override
    public void save() {
        super.save();
        Date historyDate = new Date();
        wrapValidate();
        getBaseDAO().save(this);

        if (newHistory == null)
            newHistory = createHistory();
        if (newHistory != null)
            setHistoryAudit(newHistory, historyDate);
        /*
         * Persists/Inserts the primary entity and audit entity
         * (i.e. saves the records) to the database. DTML.
         */
        getBaseDAO().saveWithHistory(this, newHistory);
    }

    protected void wrapValidate() {
        try {
            validate();
        } catch (ApplicationValidationException validationException) {
            logger.error("Validation failed with " + validationException.getErrorCode());
            throw new ApplicationRuntimeException(validationException);
        }
    }
//
//    @Transient
//    public EntityMode getEntityMode() {
//        if (isExpired == true)
//            return EntityMode.EXPIRED;
//        else
//            return EntityMode.UPDATEABLE;
//    }
//
//    @Transient
//    public EntityState getEntityState() {
//        if (recentHistory != null)
//            return EntityState.MODIFIED;
//
//        return EntityState.UNMODIFIED;
//    }

    @Column(name = "EXPIRED_FLG")
    @org.hibernate.annotations.Type(type = "yes_no")
    public Boolean getIsExpired() {
        return isExpired;
    }

    /**
     * Use the <code>expire</code> method to expire an entity.
     * Only use the property setter if generating entities in
     * memory for test purposes.
     *
     * @param isExpired
     */
    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    /**
     * Save a list of abstract entities within the same transaction
     *
     * @param entities
     */
    public static void save(List<AbstractEntity> entities) {
        getBaseDAO().save(entities);
    }

    public void expire() {
        if (this.isExpired) {
            // Don't try to expire an already expired entity.
            return;
        }

//        if (hasUniqueCodeProperty())
//            modifyUniqueCodeNameOnExpiry();

        this.isExpired = true;
        generateHistory();
        ExpiryPolicy expirePolicy = getBaseDAO().getExpiryPolicy(this.getClass().getSimpleName());
        expirePolicy.cascadeExpire(this);
        if (expirePolicy.isDeleteOnExpiry())
            getBaseDAO().delete(this);
    }
//
//    private void modifyUniqueCodeNameOnExpiry() {
//        UniqueNameModifier uniqueNameModifier = new UniqueNameModifier(
//                fetchQueryEntityName(),
//                fetchUniqueCodeQueryProperty(),
//                fetchUniqueCodePropertyMaximumLength());
//
//        String codeSetToExpired = uniqueNameModifier.modifyCode(
//                fetchUniqueCodeForExpiry(),
//                UniqueNameModifier.EXPIRED_SUFFIX);
//
//        if (codeSetToExpired == null)
//            throw new ApplicationRuntimeException(CoreTransactionErrorCode.SYSTEM_FAILURE.getCode());
//
//        setModifiedUniqueCodeOnExpiry(codeSetToExpired);
//    }
//
//    /**
//     * Override the next four methods if this domain object has a unique code or name that will be set to expired.
//     *
//     * @return
//     */
//    protected QueryProperty fetchUniqueCodeQueryProperty() {
//        return null;
//    }
//
//    protected boolean hasUniqueCodeProperty() {
//        return fetchUniqueCodeQueryProperty() != null;
//    }

    protected String fetchUniqueCodeForExpiry() {
        return null;
    }

    protected void setModifiedUniqueCodeOnExpiry(String modifiedCode) {
    }

    protected int fetchUniqueCodePropertyMaximumLength() {
        return 20;
    }

    /**
     * Triggers the update mechanism which includes validation and the creation of history.
     */
    public void update() {
        super.update();
        generateHistory();
    }

    /**
     * Triggers the update mechanism which includes validation and the creation of history.
     */
    private void generateHistory() {
        Date historyDate = new Date();

        if (recentHistory == null) {
            recentHistory = fetchRecentHistory();
        }
        if (recentHistory != null) {
            recentHistory.setIsRecentHistory(true);
            recentHistory.getHistoryDateTimeStamp().setValidToDateTime(historyDate);
            getBaseDAO().flush();
        }
        if (newHistory == null) {
            newHistory = createHistory();
            if (newHistory != null) {
                getBaseDAO().save(newHistory);

            }
        }
        if (newHistory != null) {
            setHistoryAudit(newHistory, historyDate);
        }
    }

    private void setHistoryAudit(AuditAbstractEntity entity, Date historyDate) {
        String userName = getAuditManager().getCurrentAuditUserName();
        String auditComments = getAuditManager().getCurrentAuditComments();
        entity.setAuditComments(auditComments);
        entity.getHistoryDateTimeStamp().setLastUserid(userName);
        entity.getHistoryDateTimeStamp().setValidFromDateTime(historyDate);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(4546, 0, 31, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        entity.getHistoryDateTimeStamp().setValidToDateTime(calendar.getTime());
    }

    /**
     * Override the default null operation to create a history record that will
     * be included in the create transaction.
     */
    protected abstract AuditAbstractEntity createHistory();

    /**
     * Domain classes override this method to return the most recent history record.
     *
     * @return
     */
    public abstract AuditAbstractEntity fetchRecentHistory();

    @Transient
    public Date getValidFromDateTime() {
        return fetchRecentHistory().getValidFromDateTime();
    }

    @Transient
    public Date getValidToDateTime() {
        return fetchRecentHistory().getValidToDateTime();
    }

    @Transient
    public String getLastUserid() {
        return fetchRecentHistory().getLastUserid();
    }

    @Transient
    public String getAuditComments() {
        return fetchRecentHistory().getAuditComments();
    }

    @Transient
    public abstract String fetchQueryEntityName();
}