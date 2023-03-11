package com.sss.app.core.auth.service;

import com.sss.app.core.auth.exception.UserAuthenticationException;
import com.sss.app.core.user.model.User;

import java.util.List;

public interface UserAuthorizationManager {


    public void initialize();

    public void terminate();

    public User getCurrentSignedInUser();

    public List<String> getSignedInGroupNames();

    public List<String> getRoles();

    public void currentUserSessionCleanup();

    public String getCurrentAuditUserName();

    public void setCurrentAuditUserName(String name);

    public String getCurrentAuditComments();

    public void setCurrentAuditComments(String comments);

    public void logout() throws UserAuthenticationException;
}
