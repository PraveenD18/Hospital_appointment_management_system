package com.ey.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "mySuperSecretKeyForJwtAuthentication1234567890";
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private static final String ROLE_CLAIM = "role";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim(ROLE_CLAIM, role) // e.g., "ADMIN" or "PATIENT" (no ROLE_ prefix)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    /** NEW: extract single role string from token (e.g., "PATIENT" / "ADMIN" / "DOCTOR"). */
    public String extractRole(String token) {
        Object role = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().get(ROLE_CLAIM);
        return role != null ? role.toString() : null;
    }
}