package com.mockinterview.mockinterview.SprintAI.repositories;


import com.mockinterview.mockinterview.SprintAI.entities.StandupUpdate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StandupUpdateRepository extends MongoRepository<StandupUpdate, String> {
    
    // Find standup by user and date
    Optional<StandupUpdate> findByUserIdAndDate(String userId, LocalDate date);
    
    // Find all standups for a date (for daily scrum)
    List<StandupUpdate> findBySprintIdAndDate(String sprintId, LocalDate date);
    
    // Find all standups for a user in a sprint
    List<StandupUpdate> findByUserIdAndSprintId(String userId, String sprintId);
    
    // Find all standups for a sprint
    List<StandupUpdate> findBySprintId(String sprintId);
    
    // Find standups with blockers
    @Query("{ 'sprintId': ?0, 'date': ?1, 'blockers': { $exists: true, $ne: [] } }")
    List<StandupUpdate> findStandupsWithBlockers(String sprintId, LocalDate date);
    
    // Find standups in a date range
    @Query("{ 'sprintId': ?0, 'date': { $gte: ?1, $lte: ?2 } }")
    List<StandupUpdate> findBySprintIdAndDateBetween(String sprintId, LocalDate startDate, LocalDate endDate);
    
    // Find latest standup for each user in a sprint
    @Query("{ 'sprintId': ?0 }")
    List<StandupUpdate> findLatestBySprintId(String sprintId);
    
    // Check if user has submitted standup for today
    boolean existsByUserIdAndDate(String userId, LocalDate date);
}
