package com.task.management.auth;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

@Component
public class AuthJwtToken {
    private String jwtSecret = "secretKey";
    private int jwtExpirationMs = 3600000;

    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public  boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    public Map<String, Object> extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }
    public String extractId(String token) {
        return (String) extractClaims(token).get("id");
    }
    public String extractUsername(String token) {
        return (String) extractClaims(token).get("username");
    }


}
