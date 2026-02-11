package com.mockinterview.mockinterview.SprintAI.repositories;


import com.mockinterview.mockinterview.SprintAI.enums.UserRole;
import com.mockinterview.mockinterview.SprintAI.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);

  List<User> findByTeamId(String teamId);

  List<User> findByRole(UserRole role);

  List<User> findByTeamIdAndRole(String teamId, UserRole role);

  boolean existsByEmail(String email);

  List<User> findByActiveTrue();

  long countByTeamId(String teamId);

  List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
      String name,
      String email
  );
}
