package com.fitness.tracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long expiration;

  public JwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expiration = expiration;
  }

  public String create(String email, String role) {
    return Jwts.builder()
        .subject(email)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key)
        .compact();
  }

  public String createPasswordReset(String email, String passwordHash) {
    return Jwts.builder()
        .subject(email)
        .claim("purpose", "password-reset")
        .claim("passwordVersion", fingerprint(passwordHash))
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
        .signWith(key)
        .compact();
  }

  public String passwordResetEmail(String token, String currentPasswordHash) {
    var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    if (!"password-reset".equals(claims.get("purpose", String.class))
        || !fingerprint(currentPasswordHash).equals(claims.get("passwordVersion", String.class))) {
      throw new IllegalArgumentException(
          "Password reset authorization is invalid or has already been used");
    }
    return claims.getSubject();
  }

  private String fingerprint(String value) {
    try {
      byte[] digest =
          MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to secure password reset token", e);
    }
  }

  public String email(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public boolean valid(String token) {
    try {
      var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
      return claims.getSubject() != null && claims.get("role", String.class) != null;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
