package com.mockinterview.mockinterview.SprintAI.repositories;


import com.mockinterview.mockinterview.SprintAI.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    
    Optional<Team> findByName(String name);
    
    List<Team> findByActiveTrue();
    
    List<Team> findByScrumMasterId(String scrumMasterId);
    
    boolean existsByName(String name);
}
