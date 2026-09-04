package com.fitness.tracker.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "password_reset_otps")
public class PasswordResetOtp {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(nullable = false, unique = true)
  public String email;

  @Column(nullable = false)
  public String otpHash;

  @Column(nullable = false)
  public Instant expiresAt;

  @Column(nullable = false)
  public int attempts;
}
