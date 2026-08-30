package com.fitness.tracker.service;

import com.fitness.tracker.dto.TrackerDtos.*;
import com.fitness.tracker.entity.User;

public interface AIRecommendationService {
  AIResponse recommend(User user, AIRequest request);
}
