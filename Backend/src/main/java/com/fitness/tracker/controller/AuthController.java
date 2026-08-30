package com.fitness.tracker.controller;

import com.fitness.tracker.dto.AuthDtos.*;
import com.fitness.tracker.entity.*;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@SuppressWarnings("null")
public class AuthController {
  private final UserRepository repo;
  private final PasswordEncoder encoder;
  private final AuthenticationManager auth;
  private final JwtService jwt;

  public AuthController(
      UserRepository r, PasswordEncoder e, AuthenticationManager a, JwtService j) {
    repo = r;
    encoder = e;
    auth = a;
    jwt = j;
  }

  @PostMapping("/register")
  ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest q) {
    if (repo.existsByEmail(q.email().toLowerCase()))
      throw new IllegalArgumentException("Email is already registered");
    var u = new User();
    u.name = q.name();
    u.email = q.email().toLowerCase();
    u.password = encoder.encode(q.password());
    u.age = q.age();
    u.gender = q.gender();
    u.height = q.height();
    u.weight = q.weight();
    repo.save(u);
    return ResponseEntity.status(201).body(response(u));
  }

  @PostMapping("/login")
  AuthResponse login(@Valid @RequestBody LoginRequest q) {
    auth.authenticate(
        new UsernamePasswordAuthenticationToken(q.email().toLowerCase(), q.password()));
    return response(repo.findByEmail(q.email().toLowerCase()).orElseThrow());
  }

  private AuthResponse response(User u) {
    return new AuthResponse(
        jwt.create(u.email, u.role.name()),
        new UserResponse(u.id, u.name, u.email, u.age, u.gender, u.height, u.weight, u.role));
  }
}
