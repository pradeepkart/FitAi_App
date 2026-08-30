package com.fitness.tracker.controller;

import com.fitness.tracker.entity.Exercise;
import com.fitness.tracker.exception.ApiExceptionHandler.NotFoundException;
import com.fitness.tracker.repository.ExerciseRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
@SuppressWarnings("null")
public class ExerciseController {
  private final ExerciseRepository repo;

  public ExerciseController(ExerciseRepository r) {
    repo = r;
  }

  public record Request(
      @NotBlank String name,
      String category,
      String muscleGroup,
      String description,
      @Positive Double caloriesPerMinute) {}

  @GetMapping
  List<Exercise> all() {
    return repo.findAll();
  }

  @GetMapping("/{id}")
  Exercise one(@PathVariable Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Exercise not found"));
  }

  @PostMapping
  ResponseEntity<Exercise> add(@Valid @RequestBody Request q) {
    var x = new Exercise();
    copy(x, q);
    return ResponseEntity.status(201).body(repo.save(x));
  }

  @PutMapping("/{id}")
  Exercise put(@PathVariable Long id, @Valid @RequestBody Request q) {
    var x = one(id);
    copy(x, q);
    return repo.save(x);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable Long id) {
    repo.delete(one(id));
    return ResponseEntity.noContent().build();
  }

  private void copy(Exercise x, Request q) {
    x.name = q.name();
    x.category = q.category();
    x.muscleGroup = q.muscleGroup();
    x.description = q.description();
    x.caloriesPerMinute = q.caloriesPerMinute();
  }
}
