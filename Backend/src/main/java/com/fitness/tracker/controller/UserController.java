package com.fitness.tracker.controller;

import com.fitness.tracker.dto.AuthDtos.*;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@SuppressWarnings("null")
public class UserController {
  private final CurrentUserService current;
  private final UserRepository repo;

  public UserController(CurrentUserService c, UserRepository r) {
    current = c;
    repo = r;
  }

  @GetMapping("/profile")
  UserResponse get(Authentication a) {
    return out(current.get(a));
  }

  @PutMapping("/profile")
  UserResponse update(Authentication a, @Valid @RequestBody ProfileRequest q) {
    var u = current.get(a);
    u.name = q.name();
    u.age = q.age();
    u.gender = q.gender();
    u.height = q.height();
    u.weight = q.weight();
    return out(repo.save(u));
  }

  private UserResponse out(com.fitness.tracker.entity.User u) {
    return new UserResponse(u.id, u.name, u.email, u.age, u.gender, u.height, u.weight, u.role);
  }
}
