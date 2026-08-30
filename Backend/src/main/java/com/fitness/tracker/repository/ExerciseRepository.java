package com.fitness.tracker.repository;

import com.fitness.tracker.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
  boolean existsByName(String name);
}
