package com.fitness.tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "water_intakes")
public class WaterIntake {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  public User user;

  public Integer amountMl;
  public LocalDate recordedDate;
}
