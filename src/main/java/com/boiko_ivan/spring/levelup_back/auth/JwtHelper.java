package com.boiko_ivan.spring.levelup_back.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtHelper {

    private final Key accessSecret;
    private final Key refreshSecret;
    private final long accessLifetime;
    private final long refreshLifetime;

    public JwtHelper(
            @Value("${security.jwt.secret.access}") String jwtAccessSecret,
            @Value("${security.jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${security.jwt.lifetime.access}") long accessLifetime,
            @Value("${security.jwt.lifetime.refresh}") long refreshLifetime
    ) {
        this.accessSecret = Keys.hmacShaKeyFor(jwtAccessSecret.getBytes());
        this.refreshSecret = Keys.hmacShaKeyFor(jwtRefreshSecret.getBytes());
        this.accessLifetime = accessLifetime;
        this.refreshLifetime = refreshLifetime;
    }

    public boolean accessIsExpired(String token) {
        return isExpired(token, accessSecret);
    }

    public boolean refreshIsExpired(String token) {
        return isExpired(token, refreshSecret);
    }

    private boolean isExpired(String token, Key secret) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String getSubjectFromAccessToken(String token) {
        return getSubjectFromToken(token, accessSecret);
    }

    public String getSubjectFromRefreshToken(String token) {
        return getSubjectFromToken(token, refreshSecret);
    }

    private String getSubjectFromToken(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessSecret, accessLifetime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshSecret, refreshLifetime);
    }

    private String generateToken(UserDetails userDetails, Key secret, long lifetime) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(secret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }
}