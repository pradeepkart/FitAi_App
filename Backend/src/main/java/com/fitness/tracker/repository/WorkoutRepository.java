package com.fitness.tracker.repository;

import com.fitness.tracker.entity.Workout;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
  List<Workout> findByUserIdOrderByWorkoutDateDesc(Long id);

  Optional<Workout> findByIdAndUserId(Long id, Long userId);
}
