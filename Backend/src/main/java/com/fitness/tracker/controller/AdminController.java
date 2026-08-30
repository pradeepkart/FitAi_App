package com.fitness.tracker.controller;

import com.fitness.tracker.dto.AuthDtos.UserResponse;
import com.fitness.tracker.exception.ApiExceptionHandler.NotFoundException;
import com.fitness.tracker.repository.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@SuppressWarnings("null")
public class AdminController {
  private final UserRepository users;
  private final WorkoutRepository workouts;
  private final ExerciseRepository exercises;

  public AdminController(UserRepository u, WorkoutRepository w, ExerciseRepository e) {
    users = u;
    workouts = w;
    exercises = e;
  }

  @GetMapping("/users")
  List<UserResponse> users() {
    return users.findAll().stream()
        .map(
            u ->
                new UserResponse(
                    u.id, u.name, u.email, u.age, u.gender, u.height, u.weight, u.role))
        .toList();
  }

  @GetMapping("/users/{id}")
  UserResponse user(@PathVariable Long id) {
    var u = users.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    return new UserResponse(u.id, u.name, u.email, u.age, u.gender, u.height, u.weight, u.role);
  }

  @DeleteMapping("/users/{id}")
  ResponseEntity<Void> delete(@PathVariable Long id) {
    users.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/stats")
  Map<String, Long> stats() {
    return Map.of(
        "totalUsers",
        users.count(),
        "totalWorkouts",
        workouts.count(),
        "totalExercises",
        exercises.count());
  }
}
