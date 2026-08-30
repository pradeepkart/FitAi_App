package com.fitness.tracker.dto;

import com.fitness.tracker.entity.Enums;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public final class TrackerDtos {
  private TrackerDtos() {}

  public record WorkoutRequest(
      @NotBlank String workoutName,
      @NotNull Enums.WorkoutType workoutType,
      @Positive Integer duration,
      @PositiveOrZero Integer caloriesBurned,
      @NotNull LocalDate workoutDate,
      String notes) {}

  public record WeightRequest(@Positive Double weight, @NotNull LocalDate recordedDate) {}

  public record WaterRequest(@Positive Integer amountMl, @NotNull LocalDate recordedDate) {}

  public record CalorieRequest(
      @NotBlank String foodName,
      @Positive Integer calories,
      @NotNull Enums.MealType mealType,
      @NotNull LocalDate recordedDate) {}

  public record GoalRequest(
      @NotNull Enums.GoalType goalType,
      @Positive Double targetValue,
      @PositiveOrZero Double currentValue,
      @NotNull LocalDate startDate,
      @NotNull LocalDate targetDate,
      @NotNull Enums.GoalStatus status) {}

  public record AIRequest(
      @NotBlank String fitnessGoal,
      @NotBlank String experienceLevel,
      @Min(10) @Max(240) Integer availableWorkoutMinutes,
      @Min(1) @Max(7) Integer workoutsPerWeek) {}

  public record AIResponse(
      String suggestedWorkoutPlan,
      Integer workoutFrequency,
      List<String> exercises,
      String calorieGuidance,
      String hydrationGuidance,
      String recoverySuggestions,
      String motivation,
      String disclaimer) {}
}
