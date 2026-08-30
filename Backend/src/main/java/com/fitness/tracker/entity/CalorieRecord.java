package com.fitness.tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "calorie_records")
public class CalorieRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  public User user;

  public String foodName;
  public Integer calories;

  @Enumerated(EnumType.STRING)
  public Enums.MealType mealType;

  public LocalDate recordedDate;
}
