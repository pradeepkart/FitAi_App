CREATE DATABASE IF NOT EXISTS fitness_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fitness_tracker;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  age INT,
  gender VARCHAR(50),
  height DOUBLE,
  weight DOUBLE,
  role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  UNIQUE KEY uk_users_email (email)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS exercises (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(100),
  muscle_group VARCHAR(100),
  description VARCHAR(1500),
  calories_per_minute DOUBLE,
  UNIQUE KEY uk_exercises_name (name)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS workouts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  workout_name VARCHAR(255),
  workout_type ENUM(
    'CARDIO',
    'STRENGTH',
    'CYCLING',
    'RUNNING',
    'WALKING',
    'YOGA',
    'SPORTS',
    'OTHER'
  ),
  duration INT,
  calories_burned INT,
  workout_date DATE,
  notes VARCHAR(1000),
  KEY idx_workouts_user_date (user_id, workout_date),
  CONSTRAINT fk_workouts_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS weight_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  weight DOUBLE,
  recorded_date DATE,
  KEY idx_weight_user_date (user_id, recorded_date),
  CONSTRAINT fk_weight_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS water_intakes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  amount_ml INT,
  recorded_date DATE,
  KEY idx_water_user_date (user_id, recorded_date),
  CONSTRAINT fk_water_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS calorie_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  food_name VARCHAR(255),
  calories INT,
  meal_type ENUM('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'),
  recorded_date DATE,
  KEY idx_calories_user_date (user_id, recorded_date),
  CONSTRAINT fk_calories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS fitness_goals (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  goal_type ENUM(
    'WEIGHT_LOSS',
    'WEIGHT_GAIN',
    'MAINTAIN_WEIGHT',
    'DAILY_STEPS',
    'WATER_INTAKE',
    'WORKOUT_DURATION'
  ),
  target_value DOUBLE,
  current_value DOUBLE,
  start_date DATE,
  target_date DATE,
  status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
  KEY idx_goals_user_status (user_id, status),
  CONSTRAINT fk_goals_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE = InnoDB;

INSERT IGNORE INTO
  exercises (
    name,
    category,
    muscle_group,
    description,
    calories_per_minute
  )
VALUES
  (
    'Push Ups',
    'Strength',
    'Chest and triceps',
    'Bodyweight pressing exercise.',
    7.0
  ),
  (
    'Squats',
    'Strength',
    'Legs',
    'Bodyweight lower-body exercise.',
    8.0
  ),
  (
    'Plank',
    'Core',
    'Core',
    'Isometric core stability exercise.',
    4.0
  ),
  (
    'Walking',
    'Cardio',
    'Full body',
    'Low-impact cardiovascular exercise.',
    4.5
  ),
  (
    'Running',
    'Cardio',
    'Full body',
    'Cardiovascular exercise.',
    10.0
  ),
  (
    'Cycling',
    'Cardio',
    'Legs',
    'Low-impact cardiovascular exercise.',
    8.0
  ),
  (
    'Jump Rope',
    'Cardio',
    'Full body',
    'High-intensity coordination exercise.',
    12.0
  ),
  (
    'Bench Press',
    'Strength',
    'Chest and triceps',
    'Weighted horizontal press.',
    6.0
  ),
  (
    'Deadlift',
    'Strength',
    'Posterior chain',
    'Compound hip-hinge exercise.',
    8.0
  ),
  (
    'Pull Ups',
    'Strength',
    'Back and biceps',
    'Bodyweight vertical pull.',
    8.0
  );
