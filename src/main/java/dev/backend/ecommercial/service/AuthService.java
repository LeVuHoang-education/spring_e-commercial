package dev.backend.ecommercial.service;

import dev.backend.ecommercial.exception.EmailAlreadyExistsException;
import dev.backend.ecommercial.model.dto.AuthDto;
import dev.backend.ecommercial.model.entity.Role;
import dev.backend.ecommercial.model.entity.User;
import dev.backend.ecommercial.model.payload.request.SignIn;
import dev.backend.ecommercial.model.payload.request.SignUp;
import dev.backend.ecommercial.model.payload.response.ResponseData;
import dev.backend.ecommercial.model.payload.response.ResponseError;
import dev.backend.ecommercial.repository.RoleRepository;
import dev.backend.ecommercial.repository.UserRepository;
import dev.backend.ecommercial.security.CustomUserDetailsService;
import dev.backend.ecommercial.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Log4j2
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDto login(SignIn form) {
        log.info("Starting authentication for email: {}", form.getEmail());

        User user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + form.getEmail()));

        log.info("User {} found", user.getEmail());

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            log.error("Authentication failed for email: {}", form.getEmail());
            throw new IllegalArgumentException("Invalid email/password supplied");
        }

        log.info("Password matched for email: {}", form.getEmail());

        UserDetails userDetails = new CustomUserDetailsService(userRepository).loadUserByUsername(form.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, form.getPassword(), userDetails.getAuthorities());

        authentication = authenticationManager.authenticate(authentication);
        log.info("Authentication successful for email: {}", form.getEmail());

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return AuthDto.from(user, accessToken, refreshToken, "success", "Login successfully");
    }

    public ResponseData<String> register(SignUp form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        Role role = roleRepository.findByName("ROLE_USER").orElse(null);
        if (role == null) {
            return new ResponseError<>(404, "Not found role ROLE_USER");
        }

        User user = User.builder()
                .email(form.getEmail())
                .userName(form.getUsername())
                .phone(form.getPhone())
                .password(passwordEncoder.encode(form.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        log.info("User {} registered successfully", user.getEmail());
        return new ResponseData<>(200, "Register successfully", "id: " + user.getUserId());
    }

    public AuthDto refreshJWT(String refreshToken) {
        if (refreshToken != null) {
            refreshToken = refreshToken.replace("Bearer ", "");
            if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
                Authentication auth = jwtTokenProvider.createAuthentication(refreshToken);

                User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->
                        new IllegalArgumentException("User not found with email: " + auth.getName()));
                String status = "success";
                String result = "Refresh token successfully";

                return AuthDto.from(user, jwtTokenProvider.generateAccessToken(auth), refreshToken, status, result);
            }
        }
        throw new IllegalArgumentException("Invalid refresh token");
    }

    public ResponseData<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.getJwtFromRequest(request);
        if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
            Date expiryDateFromToken = jwtTokenProvider.getExpiryDateFromToken(accessToken);
            LocalDateTime expiryDate = expiryDateFromToken.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            response.setHeader("Set-Cookie", "JSESSIONID=; HttpOnly; Path=/; Max-Age=0; Secure; SameSite=Strict");
            return new ResponseData<>(200, "Logout successfully", null);
        }
        return new ResponseError<>(400, "Token không hợp lệ hoặc đã hết hạn");
    }
}