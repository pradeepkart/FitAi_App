package com.fitness.tracker.repository;

import com.fitness.tracker.entity.CalorieRecord;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalorieRecordRepository extends JpaRepository<CalorieRecord, Long> {
  List<CalorieRecord> findByUserIdOrderByRecordedDateDesc(Long id);

  Optional<CalorieRecord> findByIdAndUserId(Long id, Long userId);
}
