package com.sss.app.core.auth.serviceimpl;

import com.sss.app.core.auth.service.UserAuthorizationManager;
import com.sss.app.core.user.model.ApplicationUser;
import com.sss.app.core.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUserAuthorizationManagerMock extends ApplicationUserAuthorizationManager implements UserAuthorizationManager {

    public static final String NAME = "fred";

    @Override
    protected User getInternalUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        List<String> scopes = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        List<String> groups = new ArrayList<>();
        boolean isAdministrator = false;
        for (GrantedAuthority authority : context.getAuthentication().getAuthorities()) {
            if (authority.getAuthority().startsWith("ROLE")) {
                String roleStr = authority.getAuthority().substring(5);
                roles.add(roleStr);
            } else {
                groups.add(authority.getAuthority());
            }
            if (authority.getAuthority().endsWith("_ADMIN"))
                isAdministrator = true;
        }

        String name = NAME;
        String displayName = name;

        return new ApplicationUser(
                name,
                displayName,
                roles,
                groups,
                isAdministrator);
    }

    @Override
    public synchronized void initialize() {
        super.initialize();
    }
}
