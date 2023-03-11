package com.sss.app.core.auth.config;

import com.sss.app.core.auth.service.UserAuthorizationManager;
import com.sss.app.core.auth.serviceimpl.ApplicationUserAuthorizationManagerMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class AuthorizationConfig {
    @Primary
    @Bean
    public UserAuthorizationManager userAuthorizationManager() {
        return new ApplicationUserAuthorizationManagerMock();
    }

}
