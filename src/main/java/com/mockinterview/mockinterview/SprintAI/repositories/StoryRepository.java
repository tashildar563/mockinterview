package com.mockinterview.mockinterview.SprintAI.repositories;


import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
    
    List<Story> findBySprintId(String sprintId);
    
    List<Story> findBySprintIdAndStatus(String sprintId, StoryStatus status);
    
    List<Story> findByAssignedTo(String userId);
    
    List<Story> findByAssignedToAndStatus(String userId, StoryStatus status);
    
    // Find stories with blockers
    @Query("{ 'sprintId': ?0, 'blockers': { $exists: true, $ne: [] } }")
    List<Story> findBlockedStoriesBySprintId(String sprintId);
    
    // Find stories completed on a specific date
    @Query("{ 'sprintId': ?0, 'completedAt': { $gte: ?1, $lt: ?2 } }")
    List<Story> findStoriesCompletedOnDate(String sprintId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    // Count stories by status
    long countBySprintIdAndStatus(String sprintId, StoryStatus status);
    
    // Sum story points by status
    @Query(value = "{ 'sprintId': ?0, 'status': ?1 }", fields = "{ 'points': 1 }")
    List<Story> findPointsBySprintIdAndStatus(String sprintId, StoryStatus status);
    
    // Find all stories assigned to a user in active sprints
    List<Story> findByAssignedToAndStatusIn(String userId, List<StoryStatus> statuses);
}
