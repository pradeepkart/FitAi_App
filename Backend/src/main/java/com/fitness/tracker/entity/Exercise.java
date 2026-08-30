package com.fitness.tracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exercises")
public class Exercise {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(nullable = false, unique = true)
  public String name;

  public String category;
  public String muscleGroup;

  @Column(length = 1500)
  public String description;

  public Double caloriesPerMinute;
}
