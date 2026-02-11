package com.mockinterview.mockinterview.SprintAI.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse {
    
    private String userId;
    private String userName;
    private String userEmail;
    private String role;
    
    // Story Assignment
    private Long assignedStories;
    private Long completedStories;
    private Long inProgressStories;
    
    // Task Assignment
    private Long assignedTasks;
    private Long completedTasks;
    private Long inProgressTasks;
    private Long todoTasks;
    
    // Story Points
    private Integer totalStoryPoints;
    private Integer completedStoryPoints;
    
    // Productivity
    private Integer averageTaskCompletionHours;
    private Double taskCompletionRate; // Percentage
    
    // Current Workload
    private Long activeTasksCount;
    private Long activeStoriesCount;
}
