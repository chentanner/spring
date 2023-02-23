package com.sss.app.core.entity.managers;

import com.sss.app.core.entity.model.AuditManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ApplicationAuditManager implements AuditManager {
    private static final Logger logger = LogManager.getLogger();
    private static volatile ThreadLocal<AuditHolder> auditHolder;
    private static final int USER_NAME_LENGTH = 30;

    public String getCurrentAuditUserName() {
        AuditHolder currentHolder = getCurrentAuditHolder();
        return currentHolder.getAuditUser();
    }

    public void setCurrentAuditUserName(String name) {
        AuditHolder currentHolder = getCurrentAuditHolder();
        currentHolder.setAuditUser(name);
    }

    public Date getCurrentHistoryDate() {
        AuditHolder currentHolder = getCurrentAuditHolder();
        return currentHolder.getHistoryDate();
    }

    public void setCurrentHistoryDate(Date date) {
        AuditHolder currentHolder = getCurrentAuditHolder();
        currentHolder.setHistoryDate(date);
    }

    public void setCurrentAuditComments(String comments) {
        AuditHolder currentHolder = getCurrentAuditHolder();
        currentHolder.setAuditComments(comments);
    }

    public String getCurrentAuditComments() {
        AuditHolder currentHolder = getCurrentAuditHolder();
        return currentHolder.getAuditComments();
    }

    private AuditHolder getCurrentAuditHolder() {
        if (auditHolder == null)
            auditHolder = new ThreadLocal<>();

        if (auditHolder.get() == null)
            auditHolder.set(new AuditHolder(new Date()));

        return auditHolder.get();
    }

    public void currentUserSessionCleanup() {

        if (auditHolder != null)
            auditHolder.set(null);
    }

    public static class AuditHolder {
        private Date historyDate;
        private String auditComments = "";
        private String auditUser;

        public AuditHolder(Date historyDate) {
            super();
            this.historyDate = historyDate;
        }

        public Date getHistoryDate() {
            return historyDate;
        }

        public void setHistoryDate(Date historyDate) {
            this.historyDate = historyDate;
        }

        public void setAuditComments(String comments) {
            this.auditComments = comments;
        }

        public String getAuditComments() {
            return auditComments;
        }

        public String getAuditUser() {
            return auditUser;
        }

        public void setAuditUser(String auditUser) {
            this.auditUser = auditUser;
        }
    }

}