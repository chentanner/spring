package com.sss.restcontroller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    @GetMapping("/public/user/hello")
    public String publicHello() {
        return "Hello, Public user!";
    }

    @GetMapping("/user/hello")
    public String hello(Principal principal) {
        return "Hello, " + principal.getName() + "!";
    }
}
