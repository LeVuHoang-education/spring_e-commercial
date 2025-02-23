package dev.backend.ecommercial.controller;

import dev.backend.ecommercial.model.payload.request.SignIn;
import dev.backend.ecommercial.model.payload.request.SignUp;
import dev.backend.ecommercial.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("**")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController{
    @Autowired
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SignIn SignIn) {
        return ResponseEntity.ok(authService.login(SignIn));
    };

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignUp SignUp) {
        return ResponseEntity.ok(authService.register(SignUp));
    };

    @GetMapping("/refresh")
    public ResponseEntity refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refreshJWT(refreshToken));
    };

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(request, response));
    };
}
