package dev.backend.ecommercial.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController<SignIn> {
    @PostMapping("/login")
    public Void authenticateUser(@RequestBody SignIn SignIn) {
        return null;
    };
}
