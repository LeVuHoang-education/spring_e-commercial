package dev.backend.ecommercial.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.backend.ecommercial.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserService userDetailsService;


    @PostConstruct
    private void validateSecretKeys() {
        if (jwtProperties.getAccessSecret() == null || jwtProperties.getAccessSecret().isEmpty()) {
            throw new IllegalArgumentException("Access Secret cannot be null or empty");
        }
        if (jwtProperties.getRefreshSecret() == null || jwtProperties.getRefreshSecret().isEmpty()) {
            throw new IllegalArgumentException("Refresh Secret cannot be null or empty");
        }
    }

    @PostConstruct
    private void logJwtProperties() {
        log.info("Loaded JWT Properties:");
        log.info("Access Secret: {}", jwtProperties.getAccessSecret());
        log.info("Refresh Secret: {}", jwtProperties.getRefreshSecret());
        log.info("Token Prefix: {}", jwtProperties.getPrefix());
        log.info("Auth Header: {}", jwtProperties.getAuthHeader());
        log.info("Access Expiry: {}", jwtProperties.getAccessExpiresAt());
        log.info("Refresh Expiry: {}", jwtProperties.getRefreshExpiresAt());
    }

    public String generateAccessToken(Authentication auth) {
        return JWT.create()
                .withExpiresAt(Instant.now().plusMillis(jwtProperties.getAccessExpiresAt()))
                .withSubject(auth.getName())
                .withClaim("role", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null))
                .sign(Algorithm.HMAC512(jwtProperties.getAccessSecret()));
    }

    // Generate refresh token
    public String generateRefreshToken(Authentication auth) {
        return JWT.create()
                .withExpiresAt(Instant.now().plusMillis(jwtProperties.getRefreshExpiresAt()))
                .withIssuedAt(Instant.now())
                .withSubject(auth.getName())
                .sign(Algorithm.HMAC512(jwtProperties.getRefreshSecret()));
    }

    public boolean validateAccessToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(jwtProperties.getAccessSecret()))
                    .acceptExpiresAt(jwtProperties.getAccessExpiresAt())
                    .withClaimPresence("sub")
                    .withClaimPresence("role")
                    .build()
                    .verify(token);

            String username = jwt.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!userDetails.isEnabled()) {
                return false;
            }

            String tokenRole = jwt.getClaim("role").asString();
            if (tokenRole == null) {
                return false;
            }

            return userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals(tokenRole));
        } catch (Exception e) {
            log.error("Error validating access token: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(jwtProperties.getRefreshSecret()))
                    .acceptExpiresAt(jwtProperties.getRefreshExpiresAt())
                    .withClaimPresence("sub")
                    .withClaimPresence("iat")
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication createAuthentication(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String username = jwt.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
    }

    public Date getExpiryDateFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt(); // Lấy ngày hết hạn từ token
    }

}
