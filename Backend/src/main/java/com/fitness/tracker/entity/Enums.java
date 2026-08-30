package com.fitness.tracker.entity;

public final class Enums {
  private Enums() {}

  public enum Role {
    USER,
    ADMIN
  }

  public enum WorkoutType {
    CARDIO,
    STRENGTH,
    CYCLING,
    RUNNING,
    WALKING,
    YOGA,
    SPORTS,
    OTHER
  }

  public enum MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
  }

  public enum GoalType {
    WEIGHT_LOSS,
    WEIGHT_GAIN,
    MAINTAIN_WEIGHT,
    DAILY_STEPS,
    WATER_INTAKE,
    WORKOUT_DURATION
  }

  public enum GoalStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
  }
}
