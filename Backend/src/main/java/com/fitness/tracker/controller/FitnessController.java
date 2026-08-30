package com.fitness.tracker.controller;

import com.fitness.tracker.dto.TrackerDtos.*;
import com.fitness.tracker.repository.WeightRecordRepository;
import com.fitness.tracker.service.*;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FitnessController {
  private final CurrentUserService current;
  private final WeightRecordRepository weights;
  private final AIRecommendationService ai;

  public FitnessController(
      CurrentUserService c, WeightRecordRepository w, AIRecommendationService a) {
    current = c;
    weights = w;
    ai = a;
  }

  @GetMapping("/fitness/bmi")
  Map<String, Object> bmi(Authentication a) {
    var u = current.get(a);
    double w =
        weights.findFirstByUserIdOrderByRecordedDateDesc(u.id).map(x -> x.weight).orElse(u.weight);
    if (u.height == null || w <= 0)
      throw new IllegalArgumentException("Height and weight are required");
    double b = Math.round(w / Math.pow(u.height / 100, 2) * 10) / 10.0;
    String cat = b < 18.5 ? "Underweight" : b < 25 ? "Normal" : b < 30 ? "Overweight" : "Obese";
    return Map.of("bmi", b, "category", cat);
  }

  @PostMapping("/ai/recommendation")
  AIResponse ai(Authentication a, @Valid @RequestBody AIRequest q) {
    return ai.recommend(current.get(a), q);
  }
}
