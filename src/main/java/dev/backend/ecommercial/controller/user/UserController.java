package dev.backend.ecommercial.controller.user;


import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@CrossOrigin("**")
@RequestMapping("/api/v1/user/profile")
public class UserController {
    private final UserService userService;

    @GetMapping("/get-profile/{userId}")
    public ResponseEntity getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @GetMapping("/get-user")
    public ResponseEntity<ResponseData> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication));
    }
}
