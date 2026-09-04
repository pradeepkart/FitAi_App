package com.fitness.tracker.dto;

import com.fitness.tracker.entity.Enums;
import jakarta.validation.constraints.*;

public final class AuthDtos {
  private AuthDtos() {}

  public record RegisterRequest(
      @NotBlank String name,
      @Email @NotBlank String email,
      @Size(min = 8) String password,
      @Min(13) @Max(120) Integer age,
      String gender,
      @Positive Double height,
      @Positive Double weight) {}

  public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

  public record ForgotPasswordRequest(@Email @NotBlank String email) {}

  public record ResetPasswordRequest(
      @NotBlank String token, @Size(min = 8) String password) {}

  public record MessageResponse(String message) {}

  public record UserResponse(
      Long id,
      String name,
      String email,
      Integer age,
      String gender,
      Double height,
      Double weight,
      Enums.Role role) {}

  public record AuthResponse(String token, UserResponse user) {}

  public record ProfileRequest(
      @NotBlank String name,
      @Min(13) @Max(120) Integer age,
      String gender,
      @Positive Double height,
      @Positive Double weight) {}
}
