package com.fitness.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(nullable = false)
  public String name;

  @Column(nullable = false, unique = true)
  public String email;

  @JsonIgnore
  @Column(nullable = false)
  public String password;

  public Integer age;
  public String gender;
  public Double height;
  public Double weight;

  @Enumerated(EnumType.STRING)
  public Enums.Role role = Enums.Role.USER;

  public Instant createdAt = Instant.now();
}
