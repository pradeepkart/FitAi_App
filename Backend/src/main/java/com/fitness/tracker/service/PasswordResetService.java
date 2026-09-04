package com.fitness.tracker.service;

import com.fitness.tracker.entity.User;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.security.JwtService;
import io.jsonwebtoken.JwtException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  private final JavaMailSender mailSender;
  private final String frontendUrl;
  private final String mailUsername;

  public PasswordResetService(
      UserRepository users,
      PasswordEncoder encoder,
      JwtService jwt,
      JavaMailSender mailSender,
      @Value("${app.frontend-url:http://localhost:5173}") String frontendUrl,
      @Value("${spring.mail.username:}") String mailUsername) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
    this.mailSender = mailSender;
    this.frontendUrl = frontendUrl.replaceAll("/+$", "");
    this.mailUsername = mailUsername;
  }

  public void sendResetLink(String email) {
    var user = users.findByEmail(email.toLowerCase()).orElse(null);
    if (user == null) return;
    if (mailUsername.isBlank()) {
      throw new IllegalStateException("Password reset email is not configured");
    }

    String token = jwt.createPasswordReset(user.email, user.password);
    String link =
        frontendUrl
            + "/reset-password?token="
            + URLEncoder.encode(token, StandardCharsets.UTF_8);
    var message = new SimpleMailMessage();
    message.setFrom(mailUsername);
    message.setTo(user.email);
    message.setSubject("Reset your FitAI password");
    message.setText(
        "Use this link to choose a new password:\n\n"
            + link
            + "\n\nThis link expires in 15 minutes. If you did not request it, ignore this email.");
    mailSender.send(message);
  }

  @Transactional
  public void resetPassword(String token, String newPassword) {
    try {
      String unverifiedEmail = jwt.email(token);
      User user =
          users
              .findByEmail(unverifiedEmail)
              .orElseThrow(() -> new IllegalArgumentException("Password reset link is invalid"));
      jwt.passwordResetEmail(token, user.password);
      user.password = encoder.encode(newPassword);
      users.save(user);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Password reset link is invalid or expired");
    }
  }
}
