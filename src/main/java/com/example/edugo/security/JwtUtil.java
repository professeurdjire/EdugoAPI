package com.example.edugo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    @Value("${app.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        // In development mode, return a default user
        if (isDevMode()) {
            return "devuser@example.com";
        }
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        // In development mode, return a future date
        if (isDevMode()) {
            return new Date(System.currentTimeMillis() + 3600000); // 1 hour from now
        }
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // In development mode, return default claims
        if (isDevMode()) {
            return claimsResolver.apply(Jwts.claims().setSubject("devuser@example.com"));
        }
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // In development mode, return empty claims
        if (isDevMode()) {
            return Jwts.claims().setSubject("devuser@example.com");
        }
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        // In development mode, tokens never expire
        if (isDevMode()) {
            return false;
        }
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        // In development mode, return a dummy token
        if (isDevMode()) {
            return "dummy-dev-token";
        }
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        // In development mode, return a dummy refresh token
        if (isDevMode()) {
            return "dummy-dev-refresh-token";
        }
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        // In development mode, return a dummy token
        if (isDevMode()) {
            return "dummy-dev-token";
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        // In development mode, return a dummy refresh token
        if (isDevMode()) {
            return "dummy-dev-refresh-token";
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // In development mode, all tokens are valid
        if (isDevMode()) {
            return true;
        }
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token) {
        // In development mode, all tokens are valid
        if (isDevMode()) {
            return true;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isDevMode() {
        String profile = System.getProperty("spring.profiles.active", "");
        boolean isDevMode = profile.contains("dev") || profile.contains("development");
        
        String devModeEnv = System.getenv("DEV_MODE");
        boolean isDevModeEnv = "true".equalsIgnoreCase(devModeEnv);
        
        return isDevMode || isDevModeEnv;
    }
}