package com.fitness.tracker.repository;

import com.fitness.tracker.entity.FitnessGoal;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessGoalRepository extends JpaRepository<FitnessGoal, Long> {
  List<FitnessGoal> findByUserIdOrderByStartDateDesc(Long id);

  Optional<FitnessGoal> findByIdAndUserId(Long id, Long userId);
}
