package com.sanjay.PandasClothing.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/current-user")
    public String currentUser(Authentication authentication) {

        if (authentication == null) {
            return "";
        }

        return authentication.getName();  // returns email
    }

}