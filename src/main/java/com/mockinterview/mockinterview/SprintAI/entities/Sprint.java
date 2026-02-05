package com.mockinterview.mockinterview.SprintAI.entities;

import com.mockinterview.mockinterview.SprintAI.enums.SprintStatus;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "sprints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    
    @Id
    private String id;
    
    private String teamId; // Reference to Team
    
    private String name; // e.g., "Sprint 23"
    
    private Integer sprintNumber; // Auto-increment per team
    
    private String goal; // Sprint goal/objective
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private SprintStatus status;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private LocalDateTime completedAt;
    
    // Metrics - calculated fields
    private Integer totalStoryPoints = 0;
    private Integer completedStoryPoints = 0;
    private Integer velocity = 0; // Points completed
    
    // Constructor for creating new sprints
    public Sprint(String teamId, String name, Integer sprintNumber, String goal, 
                  LocalDate startDate, LocalDate endDate) {
        this.teamId = teamId;
        this.name = name;
        this.sprintNumber = sprintNumber;
        this.goal = goal;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = SprintStatus.PLANNED;
        this.createdAt = LocalDateTime.now();
        this.totalStoryPoints = 0;
        this.completedStoryPoints = 0;
        this.velocity = 0;
    }
    
    // Helper methods
    public void calculateMetrics(List<Story> stories) {
        this.totalStoryPoints = stories.stream()
            .mapToInt(Story::getPoints)
            .sum();
        
        this.completedStoryPoints = stories.stream()
            .filter(s -> s.getStatus() == StoryStatus.DONE)
            .mapToInt(Story::getPoints)
            .sum();
        
        if (this.status == SprintStatus.COMPLETED) {
            this.velocity = this.completedStoryPoints;
        }
    }
    
    public double getCompletionRate() {
        if (totalStoryPoints == 0) return 0.0;
        return (double) completedStoryPoints / totalStoryPoints * 100;
    }
    
    public void markAsCompleted() {
        this.status = SprintStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
