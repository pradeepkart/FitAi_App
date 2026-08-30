package com.fitness.tracker.repository;

import com.fitness.tracker.entity.WeightRecord;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
  List<WeightRecord> findByUserIdOrderByRecordedDateDesc(Long id);

  Optional<WeightRecord> findByIdAndUserId(Long id, Long userId);

  Optional<WeightRecord> findFirstByUserIdOrderByRecordedDateDesc(Long id);
}
