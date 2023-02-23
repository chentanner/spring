package com.sss.app.core.entity.model;

import java.util.Date;

public interface AuditManager {

    public String getCurrentAuditComments();

    public void setCurrentAuditComments(String comments);

    public Date getCurrentHistoryDate();

    public void setCurrentHistoryDate(Date date);

    public String getCurrentAuditUserName();

    public void setCurrentAuditUserName(String name);

    public void currentUserSessionCleanup();

}
