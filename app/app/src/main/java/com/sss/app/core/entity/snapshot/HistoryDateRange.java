package com.sss.app.core.entity.snapshot;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistoryDateRange implements Serializable {
    private static final long serialVersionUID = 1L;

    LocalDateTime validFromDate;
    LocalDateTime validToDate;
    String lastUser;
    String auditComment = "";

    public HistoryDateRange() {
    }

    public HistoryDateRange(LocalDateTime validFromDate, LocalDateTime validToDate, String lastUser, String auditComments) {
        super();
        this.validFromDate = validFromDate;
        this.validToDate = validToDate;
        this.lastUser = lastUser;
        this.auditComment = auditComments;
    }

    public LocalDateTime getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(LocalDateTime validFromDate) {
        this.validFromDate = validFromDate;
    }

    public LocalDateTime getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(LocalDateTime validToDate) {
        this.validToDate = validToDate;
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public String getAuditComment() {
        return auditComment;
    }

    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
}
