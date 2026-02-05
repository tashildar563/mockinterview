package com.mockinterview.mockinterview.SprintAI.repositories;


import com.mockinterview.mockinterview.SprintAI.enums.SprintStatus;
import com.mockinterview.mockinterview.SprintAI.entities.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends MongoRepository<Sprint, String> {
    
    List<Sprint> findByTeamId(String teamId);
    
    List<Sprint> findByTeamIdAndStatus(String teamId, SprintStatus status);
    
    Optional<Sprint> findByTeamIdAndSprintNumber(String teamId, Integer sprintNumber);
    
    // Find active sprint for a team (current date between start and end)
    @Query("{ 'teamId': ?0, 'status': 'ACTIVE', 'startDate': { $lte: ?1 }, 'endDate': { $gte: ?1 } }")
    Optional<Sprint> findActiveSprint(String teamId, LocalDate currentDate);
    
    // Find all sprints ordered by sprint number descending
    List<Sprint> findByTeamIdOrderBySprintNumberDesc(String teamId);
    
    // Get the latest sprint number for a team
    @Query(value = "{ 'teamId': ?0 }", sort = "{ 'sprintNumber': -1 }")
    Optional<Sprint> findLatestSprintByTeamId(String teamId);
    
    // Find completed sprints for velocity calculation
    List<Sprint> findByTeamIdAndStatusOrderBySprintNumberDesc(String teamId, SprintStatus status);
    
    long countByTeamId(String teamId);
}
