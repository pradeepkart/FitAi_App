package com.fitness.tracker.repository;

import com.fitness.tracker.entity.PasswordResetOtp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
  Optional<PasswordResetOtp> findByEmail(String email);
}
