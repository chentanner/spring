package com.sss.app.core.entity.model;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(value = {AuditTableListener.class})
public abstract class AuditAbstractEntity extends AbstractIdEntity implements VersionedAuditEntity {
    private Boolean isExpired = Boolean.FALSE;


    private Long auditVersion;
    private Long entityVersion;


    @Version
    @Column(name = "AUDIT_LOCK_NO")
    public Long getVersion() {
        return auditVersion;
    }

    public void setVersion(Long version) {
        this.auditVersion = version;
    }


    @Column(name = "OPTIMISTIC_LOCK_NO")
    public Long getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(Long entityVersion) {
        this.entityVersion = entityVersion;
    }


    private boolean isRecentHistory = false;
    protected HistoryDateTimeStamp historyDateTimeStamp = new HistoryDateTimeStamp();
    protected String auditComments;

    @Transient
    public abstract TemporalAbstractEntity getParent();

    public abstract void copyFrom(TemporalAbstractEntity entity);

    protected void recordHistory() {
        this.setIsRecentHistory(true); //
        getBaseDAO().recordHistory(this);
    }


    public void prepareForImport(AuditAbstractEntity entity) {
        if (getParent().getVersion() == null) {
            getParent().setVersion(0L);
        }
    }
//
//    @Transient
//    public EntityMode getEntityMode() {
//        return EntityMode.HISTORY;
//    }
//
//    @Transient
//    public EntityState getEntityState() {
//        return EntityState.UNMODIFIED;
//    }

    @Transient
    public boolean isRecentHistory() {
        return isRecentHistory;
    }

    public void setIsRecentHistory(boolean is) {
        this.isRecentHistory = is;
    }

    @Transient
    public String getLastUserid() {
        return getHistoryDateTimeStamp().getLastUserid();
    }

    @Transient
    public Date getValidFromDateTime() {
        return getHistoryDateTimeStamp().getValidFromDateTime();
    }

    @Transient
    public Date getValidToDateTime() {
        return getHistoryDateTimeStamp().getValidToDateTime();
    }

//    /**
//     * Returns true if this audit record is the current record
//     *
//     * @return
//     */
//    @Transient
//    public boolean isCurrent() {
//        return DateUtils.equalsDate(historyDateTimeStamp.getValidToDateTime(), DateUtils.getValidToDateTimeMarker());
//    }

    @Embedded
    public HistoryDateTimeStamp getHistoryDateTimeStamp() {
        return historyDateTimeStamp;
    }

    public void setHistoryDateTimeStamp(HistoryDateTimeStamp historyDateTimeStamp) {
        this.historyDateTimeStamp = historyDateTimeStamp;
    }

    @Column(name = "AUDIT_COMMENTS_TXT")
    public String getAuditComments() {
        return auditComments;
    }

    public void setAuditComments(String auditComments) {
        this.auditComments = auditComments;
    }

    @Column(name = "EXPIRED_FLG")
    @org.hibernate.annotations.Type(type = "yes_no")
    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }
//
//    @SuppressWarnings("unchecked")
//    public static List<HistoryDateRange> getHistoryDates(TemporalAbstractEntity temporalEntity, String queryEntityName) {
//        String primaryDomainName = queryEntityName.substring(0, 1).toLowerCase() + queryEntityName.substring(1,
//                                                                                                             queryEntityName.length());
//        QueryProperty primaryProperty = new QueryProperty(queryEntityName,
//                                                          "primaryEntity",
//                                                          primaryDomainName,
//                                                          QueryPropertyType.STRING);
//        QueryProperty orderProperty = new QueryProperty(queryEntityName,
//                                                        "orderByFromDate",
//                                                        "historyDateTimeStamp.validFromDateTime",
//                                                        QueryPropertyType.STRING);
//
//        QueryCriteria queryCriteria = new QueryCriteria();
//        queryCriteria.setAuditQueryEntity(queryEntityName);
//        WhereExpression whereExpression = new WhereExpression(primaryProperty,
//                                                              ExpressionOperator.EQUALS,
//                                                              temporalEntity);
//        queryCriteria.addWhereExpression(whereExpression);
//        queryCriteria.addOrderExpression(new OrderExpression(OrderOperator.DESCENDING, orderProperty));
//        return getBaseDAO().executeReportQueryStringWithNamedParms(queryCriteria.generateAuditHistoryDateRangeQuery(),
//                                                                   queryCriteria.getWhereClause().getQueryParameters());
//    }
}