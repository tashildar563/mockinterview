package com.mockinterview.mockinterview.SprintAI.services;


import com.mockinterview.mockinterview.SprintAI.entities.User;
import com.mockinterview.mockinterview.SprintAI.repositories.UserRepository;
import com.mockinterview.mockinterview.SprintAI.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Register a new user
   */
  public User registerUser(RegisterRequest request) {
    // Check if email already exists
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already exists: " + request.getEmail());
    }

    // Create new user
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
    user.setRole(request.getRole());
    user.setTeamId(request.getTeamId());
    user.setCreatedAt(LocalDateTime.now());
    user.setActive(true);

    // Save and return
    return userRepository.save(user);
  }

  /**
   * Find user by email
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Find user by ID
   */
  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  /**
   * Get all users
   */
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Get users by team
   */
  public List<User> getUsersByTeam(String teamId) {
    return userRepository.findByTeamId(teamId);
  }

  /**
   * Update user
   */
  public User updateUser(String id, User updatedUser) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    // Update fields (don't update password or email here)
    user.setName(updatedUser.getName());
    user.setRole(updatedUser.getRole());
    user.setTeamId(updatedUser.getTeamId());

    return userRepository.save(user);
  }

  /**
   * Delete user
   */
  public void deleteUser(String id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  /**
   * Check if user exists by email
   */
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public List<User> searchUsers(String search) {
    return userRepository
        .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
  }
}
