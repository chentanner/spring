package com.sss.app.core.auth.mapperimpl;

import com.sss.app.core.auth.mapper.AuthenticationToUserMapper;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.user.model.ApplicationUser;
import com.sss.app.core.user.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ApplicationAuthenticationToUserMapper implements AuthenticationToUserMapper {
    private static final Logger logger = LogManager.getLogger();

    @Value(value = "${server.servlet.context-path:core}")
    private String applicationContextName;


    @Override
    public User map(Authentication authentication) {

        List<String> roles = new ArrayList<>();
        boolean isAdministrator = false;
        String strippedContextName = applicationContextName.substring(1);

        List<String> groups = new ArrayList<>();
        String name;
        String email;

        // this is the authentication token received from a rest call
        if (authentication instanceof JwtAuthenticationToken) {
            logger.info("JwtAuthenticationToken received");
            JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = authToken.getToken();
            email = (String) jwt.getClaims().get("email");
            name = (String) jwt.getClaims().get("user_name");

            if (name == null)
                name = (String) jwt.getClaims().get("name");

            if (name == null) {
                if (email != null)
                    name = email;
            }

            logger.info(name + " logged in");
            Collection<? extends GrantedAuthority> authorities = authToken.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("SCOPE_"))
                    continue;
                if (authority.getAuthority().startsWith("ROLE")) {
                    roles.add(authority.getAuthority());
                    continue;
                }

                if (authority.getAuthority().startsWith("SCOPE_")) {
                    String strippedAuthority = authority.getAuthority().substring(6);
                }
            }
            // This is the authentication token received from a UI call
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            logger.debug("OAuth2AuthenticationToken received");
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User user = authToken.getPrincipal();
            logger.debug("Authorities: " + user.getAuthorities());
            logger.debug("Attributes: " + user.getAttributes());

            email = (String) user.getAttributes().get("email");
            name = (String) user.getAttributes().get("user_name");
            if (name == null)
                name = (String) user.getAttributes().get("name");

            if (name == null) {
                if (email != null)
                    name = email;
            }

            if (name == null) {
                logger.error("Neither email or name set in Jwt. Reverting to principal.");
                name = authToken.getName();
            }

            logger.info(name + " logged in");
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("SCOPE_"))
                    continue;
                if (authority.getAuthority().startsWith("ROLE")) {
                    roles.add(authority.getAuthority());
                    continue;
                }

                if (authority.getAuthority().startsWith("SCOPE_")) {
                    String strippedAuthority = authority.getAuthority().substring(6);

                }
            }

        } else {
            logger.error("Unknown authentication token received");
            throw new ApplicationRuntimeException(TransactionErrorCode.AUTHENTICATION_ERROR);
        }
        logger.info("User assigned Roles: " + roles);
        logger.info("User is administrator: " + isAdministrator);
        return new ApplicationUser(
                name,
                name,
                roles,
                groups,
                isAdministrator);


    }
}
