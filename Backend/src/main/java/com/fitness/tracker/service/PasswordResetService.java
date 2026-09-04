package com.fitness.tracker.service;

import com.fitness.tracker.entity.PasswordResetOtp;
import com.fitness.tracker.entity.User;
import com.fitness.tracker.repository.PasswordResetOtpRepository;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.security.JwtService;
import io.jsonwebtoken.JwtException;
import java.security.SecureRandom;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {
  private final UserRepository users;
  private final PasswordResetOtpRepository otps;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  private final JavaMailSender mailSender;
  private final String mailUsername;
  private final SecureRandom random = new SecureRandom();

  public PasswordResetService(
      UserRepository users,
      PasswordResetOtpRepository otps,
      PasswordEncoder encoder,
      JwtService jwt,
      JavaMailSender mailSender,
      @Value("${spring.mail.username:}") String mailUsername) {
    this.users = users;
    this.otps = otps;
    this.encoder = encoder;
    this.jwt = jwt;
    this.mailSender = mailSender;
    this.mailUsername = mailUsername;
  }

  @Transactional
  public void sendOtp(String email) {
    if (mailUsername.isBlank()) {
      throw new IllegalStateException("Password reset email is not configured");
    }
    String normalizedEmail = email.toLowerCase();
    var user = users.findByEmail(normalizedEmail).orElse(null);
    if (user == null) return;

    String code = String.format("%06d", random.nextInt(1_000_000));
    PasswordResetOtp resetOtp = otps.findByEmail(normalizedEmail).orElseGet(PasswordResetOtp::new);
    resetOtp.email = normalizedEmail;
    resetOtp.otpHash = encoder.encode(code);
    resetOtp.expiresAt = Instant.now().plusSeconds(10 * 60);
    resetOtp.attempts = 0;
    otps.save(resetOtp);

    var message = new SimpleMailMessage();
    message.setFrom(mailUsername);
    message.setTo(user.email);
    message.setSubject("Your FitAI password reset code");
    message.setText(
        "Your FitAI verification code is: "
            + code
            + "\n\nThis code expires in 10 minutes. If you did not request it, ignore this email.");
    mailSender.send(message);
  }

  @Transactional
  public String verifyOtp(String email, String code) {
    String normalizedEmail = email.toLowerCase();
    PasswordResetOtp resetOtp =
        otps.findByEmail(normalizedEmail)
            .orElseThrow(() -> new IllegalArgumentException("Invalid verification code"));
    if (resetOtp.expiresAt.isBefore(Instant.now())) {
      otps.delete(resetOtp);
      throw new IllegalArgumentException("Verification code has expired");
    }
    if (resetOtp.attempts >= 5) {
      otps.delete(resetOtp);
      throw new IllegalArgumentException("Too many incorrect attempts. Request a new code");
    }
    if (!encoder.matches(code, resetOtp.otpHash)) {
      resetOtp.attempts++;
      otps.save(resetOtp);
      throw new IllegalArgumentException("Invalid verification code");
    }

    User user =
        users
            .findByEmail(normalizedEmail)
            .orElseThrow(() -> new IllegalArgumentException("Invalid verification code"));
    otps.delete(resetOtp);
    return jwt.createPasswordReset(user.email, user.password);
  }

  @Transactional
  public void resetPassword(String token, String newPassword) {
    try {
      String unverifiedEmail = jwt.email(token);
      User user =
          users
              .findByEmail(unverifiedEmail)
              .orElseThrow(
                  () -> new IllegalArgumentException("Password reset authorization is invalid"));
      jwt.passwordResetEmail(token, user.password);
      user.password = encoder.encode(newPassword);
      users.save(user);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Password reset authorization is invalid or expired");
    }
  }
}
