package com.sss.restcontroller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AppController {

    @GetMapping("/")
    String sayHello(@AuthenticationPrincipal OidcUser oidcUser) {
        return "Hello " + (oidcUser != null ? oidcUser.getFullName() : "anonymous");
    }
}
