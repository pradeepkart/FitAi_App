package com.fitness.tracker.exception;

import java.time.Instant;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(AuthenticationException.class)
  ResponseEntity<?> authentication(AuthenticationException e) {
    return body(401, "Unauthorized", "Invalid email or password");
  }

  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<?> notFound(NotFoundException e) {
    return body(404, "Not Found", e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<?> bad(IllegalArgumentException e) {
    return body(400, "Bad Request", e.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  ResponseEntity<?> unavailable(IllegalStateException e) {
    return body(503, "Service Unavailable", e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<?> validation(MethodArgumentNotValidException e) {
    String m =
        e.getBindingResult().getFieldErrors().stream()
            .map(x -> x.getField() + ": " + x.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
    return body(400, "Bad Request", m);
  }

  private ResponseEntity<?> body(int s, String er, String m) {
    return ResponseEntity.status(s)
        .body(Map.of("timestamp", Instant.now(), "status", s, "error", er, "message", m));
  }

  public static class NotFoundException extends RuntimeException {
    public NotFoundException(String m) {
      super(m);
    }
  }
}
