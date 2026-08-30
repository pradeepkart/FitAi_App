package com.fitness.tracker.controller;

import com.fitness.tracker.dto.TrackerDtos.*;
import com.fitness.tracker.entity.*;
import com.fitness.tracker.exception.ApiExceptionHandler.NotFoundException;
import com.fitness.tracker.repository.*;
import com.fitness.tracker.service.CurrentUserService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SuppressWarnings("null")
public class TrackerController {
  private final CurrentUserService current;
  private final WorkoutRepository workouts;
  private final WeightRecordRepository weights;
  private final WaterIntakeRepository water;
  private final CalorieRecordRepository calories;
  private final FitnessGoalRepository goals;

  public TrackerController(
      CurrentUserService c,
      WorkoutRepository a,
      WeightRecordRepository b,
      WaterIntakeRepository d,
      CalorieRecordRepository e,
      FitnessGoalRepository f) {
    current = c;
    workouts = a;
    weights = b;
    water = d;
    calories = e;
    goals = f;
  }

  @GetMapping("/workouts")
  List<?> workouts(Authentication a) {
    return workouts.findByUserIdOrderByWorkoutDateDesc(current.get(a).id).stream()
        .map(this::workout)
        .toList();
  }

  @GetMapping("/workouts/{id}")
  Object workout(Authentication a, @PathVariable Long id) {
    return workout(own(workouts.findByIdAndUserId(id, current.get(a).id), "Workout"));
  }

  @PostMapping("/workouts")
  ResponseEntity<?> addWorkout(Authentication a, @Valid @RequestBody WorkoutRequest q) {
    var x = new Workout();
    x.user = current.get(a);
    copy(x, q);
    return ResponseEntity.status(201).body(workout(workouts.save(x)));
  }

  @PutMapping("/workouts/{id}")
  Object putWorkout(Authentication a, @PathVariable Long id, @Valid @RequestBody WorkoutRequest q) {
    var x = own(workouts.findByIdAndUserId(id, current.get(a).id), "Workout");
    copy(x, q);
    return workout(workouts.save(x));
  }

  @DeleteMapping("/workouts/{id}")
  ResponseEntity<Void> deleteWorkout(Authentication a, @PathVariable Long id) {
    workouts.delete(own(workouts.findByIdAndUserId(id, current.get(a).id), "Workout"));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/weight")
  List<?> weights(Authentication a) {
    return weights.findByUserIdOrderByRecordedDateDesc(current.get(a).id).stream()
        .map(x -> Map.of("id", x.id, "weight", x.weight, "recordedDate", x.recordedDate))
        .toList();
  }

  @PostMapping("/weight")
  ResponseEntity<?> addWeight(Authentication a, @Valid @RequestBody WeightRequest q) {
    var x = new WeightRecord();
    x.user = current.get(a);
    x.weight = q.weight();
    x.recordedDate = q.recordedDate();
    x.user.weight = q.weight();
    return ResponseEntity.status(201).body(weights.save(x));
  }

  @PutMapping("/weight/{id}")
  Object putWeight(Authentication a, @PathVariable Long id, @Valid @RequestBody WeightRequest q) {
    var x = own(weights.findByIdAndUserId(id, current.get(a).id), "Weight record");
    x.weight = q.weight();
    x.recordedDate = q.recordedDate();
    return weights.save(x);
  }

  @DeleteMapping("/weight/{id}")
  ResponseEntity<Void> deleteWeight(Authentication a, @PathVariable Long id) {
    weights.delete(own(weights.findByIdAndUserId(id, current.get(a).id), "Weight record"));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/water")
  List<?> water(Authentication a) {
    return water.findByUserIdOrderByRecordedDateDesc(current.get(a).id).stream()
        .map(x -> Map.of("id", x.id, "amountMl", x.amountMl, "recordedDate", x.recordedDate))
        .toList();
  }

  @PostMapping("/water")
  ResponseEntity<?> addWater(Authentication a, @Valid @RequestBody WaterRequest q) {
    var x = new WaterIntake();
    x.user = current.get(a);
    x.amountMl = q.amountMl();
    x.recordedDate = q.recordedDate();
    return ResponseEntity.status(201).body(water.save(x));
  }

  @PutMapping("/water/{id}")
  Object putWater(Authentication a, @PathVariable Long id, @Valid @RequestBody WaterRequest q) {
    var x = own(water.findByIdAndUserId(id, current.get(a).id), "Water record");
    x.amountMl = q.amountMl();
    x.recordedDate = q.recordedDate();
    return water.save(x);
  }

  @DeleteMapping("/water/{id}")
  ResponseEntity<Void> deleteWater(Authentication a, @PathVariable Long id) {
    water.delete(own(water.findByIdAndUserId(id, current.get(a).id), "Water record"));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/calories")
  List<?> calories(Authentication a) {
    return calories.findByUserIdOrderByRecordedDateDesc(current.get(a).id).stream()
        .map(
            x ->
                Map.of(
                    "id",
                    x.id,
                    "foodName",
                    x.foodName,
                    "calories",
                    x.calories,
                    "mealType",
                    x.mealType,
                    "recordedDate",
                    x.recordedDate))
        .toList();
  }

  @PostMapping("/calories")
  ResponseEntity<?> addCalories(Authentication a, @Valid @RequestBody CalorieRequest q) {
    var x = new CalorieRecord();
    x.user = current.get(a);
    copy(x, q);
    return ResponseEntity.status(201).body(calories.save(x));
  }

  @PutMapping("/calories/{id}")
  Object putCalories(
      Authentication a, @PathVariable Long id, @Valid @RequestBody CalorieRequest q) {
    var x = own(calories.findByIdAndUserId(id, current.get(a).id), "Calorie record");
    copy(x, q);
    return calories.save(x);
  }

  @DeleteMapping("/calories/{id}")
  ResponseEntity<Void> deleteCalories(Authentication a, @PathVariable Long id) {
    calories.delete(own(calories.findByIdAndUserId(id, current.get(a).id), "Calorie record"));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/goals")
  List<?> goals(Authentication a) {
    return goals.findByUserIdOrderByStartDateDesc(current.get(a).id).stream()
        .map(this::goal)
        .toList();
  }

  @PostMapping("/goals")
  ResponseEntity<?> addGoal(Authentication a, @Valid @RequestBody GoalRequest q) {
    var x = new FitnessGoal();
    x.user = current.get(a);
    copy(x, q);
    return ResponseEntity.status(201).body(goal(goals.save(x)));
  }

  @PutMapping("/goals/{id}")
  Object putGoal(Authentication a, @PathVariable Long id, @Valid @RequestBody GoalRequest q) {
    var x = own(goals.findByIdAndUserId(id, current.get(a).id), "Goal");
    copy(x, q);
    return goal(goals.save(x));
  }

  @DeleteMapping("/goals/{id}")
  ResponseEntity<Void> deleteGoal(Authentication a, @PathVariable Long id) {
    goals.delete(own(goals.findByIdAndUserId(id, current.get(a).id), "Goal"));
    return ResponseEntity.noContent().build();
  }

  private <T> T own(Optional<T> x, String n) {
    return x.orElseThrow(() -> new NotFoundException(n + " not found"));
  }

  private void copy(Workout x, WorkoutRequest q) {
    x.workoutName = q.workoutName();
    x.workoutType = q.workoutType();
    x.duration = q.duration();
    x.caloriesBurned = q.caloriesBurned();
    x.workoutDate = q.workoutDate();
    x.notes = q.notes();
  }

  private void copy(CalorieRecord x, CalorieRequest q) {
    x.foodName = q.foodName();
    x.calories = q.calories();
    x.mealType = q.mealType();
    x.recordedDate = q.recordedDate();
  }

  private void copy(FitnessGoal x, GoalRequest q) {
    x.goalType = q.goalType();
    x.targetValue = q.targetValue();
    x.currentValue = q.currentValue();
    x.startDate = q.startDate();
    x.targetDate = q.targetDate();
    x.status = q.status();
  }

  private Map<String, Object> workout(Workout x) {
    var m = new LinkedHashMap<String, Object>();
    m.put("id", x.id);
    m.put("workoutName", x.workoutName);
    m.put("workoutType", x.workoutType);
    m.put("duration", x.duration);
    m.put("caloriesBurned", x.caloriesBurned);
    m.put("workoutDate", x.workoutDate);
    m.put("notes", x.notes);
    return m;
  }

  private Map<String, Object> goal(FitnessGoal x) {
    return Map.of(
        "id",
        x.id,
        "goalType",
        x.goalType,
        "targetValue",
        x.targetValue,
        "currentValue",
        x.currentValue,
        "startDate",
        x.startDate,
        "targetDate",
        x.targetDate,
        "status",
        x.status);
  }
}
