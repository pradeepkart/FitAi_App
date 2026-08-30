package com.fitness.tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "workouts")
public class Workout {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  public User user;

  public String workoutName;

  @Enumerated(EnumType.STRING)
  public Enums.WorkoutType workoutType;

  public Integer duration;
  public Integer caloriesBurned;
  public LocalDate workoutDate;

  @Column(length = 1000)
  public String notes;
}
