package com.healthcare.personal_health_monitoring.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//this class generate token
//validate token
//extract email from token

@Component
public class JWTUtil {

    private final String SECRET = "SECRET_KEY_12345";
    private final long EXPIRATION = 86400000; // 1 day

    public String generateToken(String username, String role){
        return Jwts.builder()
                .claim("role", role)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }

    public String extractRole(String token){
        return (String) getClaims(token).get("role");
    }

    public boolean isTokenValid(String token, String username){
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private Claims getClaims(String token){
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }
}
