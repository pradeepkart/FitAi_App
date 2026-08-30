package com.fitness.tracker.repository;

import com.fitness.tracker.entity.WaterIntake;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
  List<WaterIntake> findByUserIdOrderByRecordedDateDesc(Long id);

  Optional<WaterIntake> findByIdAndUserId(Long id, Long userId);
}
