package com.mockinterview.mockinterview.SprintAI.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    
    // Sprint Stats
    private Long totalSprints;
    private Long activeSprints;
    private Long completedSprints;
    private Long plannedSprints;
    
    // Story Stats
    private Long totalStories;
    private Long todoStories;
    private Long inProgressStories;
    private Long inReviewStories;
    private Long doneStories;
    private Long blockedStories;
    
    // Task Stats
    private Long totalTasks;
    private Long todoTasks;
    private Long inProgressTasks;
    private Long doneTasks;
    
    // Story Points
    private Integer totalStoryPoints;
    private Integer completedStoryPoints;
    private Integer inProgressStoryPoints;
    
    // Team Stats
    private Long totalTeamMembers;
    private Long totalTeams;
    
    // Velocity (average points completed per sprint)
    private Integer averageVelocity;
    
    // Completion Rate
    private Double completionRate; // Percentage of stories completed
    
    // Current Sprint Info (if any active sprint)
    private String currentSprintId;
    private String currentSprintName;
    private Integer currentSprintProgress; // Percentage
}
