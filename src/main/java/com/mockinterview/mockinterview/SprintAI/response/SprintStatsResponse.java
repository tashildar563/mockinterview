package com.mockinterview.mockinterview.SprintAI.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintStatsResponse {
    
    private String sprintId;
    private String sprintName;
    private String sprintGoal;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Story Counts
    private Long totalStories;
    private Long todoStories;
    private Long inProgressStories;
    private Long inReviewStories;
    private Long doneStories;
    private Long blockedStories;
    
    // Task Counts
    private Long totalTasks;
    private Long todoTasks;
    private Long inProgressTasks;
    private Long doneTasks;
    
    // Story Points
    private Integer totalStoryPoints;
    private Integer completedStoryPoints;
    private Integer remainingStoryPoints;
    
    // Progress
    private Integer completionPercentage;
    
    // Team Members
    private Long teamMembersCount;
    
    // Days
    private Integer totalDays;
    private Integer daysRemaining;
    private Integer daysElapsed;
}
