package com.sss.app.core.auth.serviceimpl;

import com.sss.app.core.auth.exception.UserAuthenticationException;
import com.sss.app.core.auth.mapper.AuthenticationToUserMapper;
import com.sss.app.core.auth.service.UserAuthorizationManager;
import com.sss.app.core.entity.model.AuditManager;
import com.sss.app.core.user.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationUserAuthorizationManager implements UserAuthorizationManager {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private AuditManager auditManager;

    @Autowired
    private AuthenticationToUserMapper authenticationToUserMapper;

    public synchronized void initialize() {

    }

    public void terminate() {
        currentUserSessionCleanup();
    }

    public void currentUserSessionCleanup() {
    }

    public boolean isSignedInUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (context.getAuthentication().isAuthenticated());
    }

    protected User getInternalUser() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authenticationToUserMapper.map(authentication);
    }

    public Authentication getAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication();
    }

    public User getCurrentSignedInUser() {

        return getInternalUser();
    }


    public List<String> getRoles() {
        return getInternalUser().getRoles();
    }

    @Override
    public List<String> getSignedInGroupNames() {
        List<String> groups = getInternalUser().getGroups();
        groups.add("default");
        return groups;
    }

    public void logout() throws UserAuthenticationException {
    }

    @Override
    public String getCurrentAuditUserName() {
        return auditManager.getCurrentAuditUserName();
    }

    @Override
    public void setCurrentAuditUserName(String name) {
        auditManager.setCurrentAuditUserName(name);
    }

    @Override
    public String getCurrentAuditComments() {
        return auditManager.getCurrentAuditComments();
    }

    @Override
    public void setCurrentAuditComments(String comments) {
        auditManager.setCurrentAuditComments(comments);

    }
}
