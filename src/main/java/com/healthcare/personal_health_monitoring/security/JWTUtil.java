//package com.healthcare.personal_health_monitoring.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JWTUtil {
//
//    private final String SECRET_KEY = "my-super-strong-secret-key-that-is-very-long!"; // 32+ chars
//    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours
//
//    private Key getSigningKey() {
//        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // Generate token
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Extract username/email from token
//    public String extractUsername(String token) {
//        return extractClaims(token).getSubject();
//    }
//
//    // Validate token for a username
//    public boolean isTokenValid(String token, String username) {
//        final String tokenUsername = extractUsername(token);
//        return (tokenUsername.equals(username) && !isTokenExpired(token));
//    }
//
//    // Check if token is expired
//    private boolean isTokenExpired(String token) {
//        return extractClaims(token).getExpiration().before(new Date());
//    }
//
//    // Extract all claims
//    private Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
