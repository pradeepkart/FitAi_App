package com.fitness.tracker.security;

import com.fitness.tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository repo;

  public CustomUserDetailsService(UserRepository r) {
    repo = r;
  }

  public UserDetails loadUserByUsername(String email) {
    var u =
        repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return org.springframework.security.core.userdetails.User.withUsername(u.email)
        .password(u.password)
        .roles(u.role.name())
        .build();
  }
}
