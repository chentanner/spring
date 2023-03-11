package com.sss.app.core.auth.mapper;

import com.sss.app.core.user.model.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationToUserMapper {
    public User map(Authentication authentication);
}
