package com.mockinterview.mockinterview.SprintAI.entities;

import com.mockinterview.mockinterview.SprintAI.enums.TaskPriority;
import com.mockinterview.mockinterview.SprintAI.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Task - Embedded document within Story
 * Represents a smaller, assignable piece of work that makes up a user story
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    private String id; // Unique task ID
    
    private String title; // Task description
    
    private String description; // Detailed task description
    
    private TaskStatus status; // TODO, IN_PROGRESS, DONE
    
    private String assignedTo; // User ID of assigned developer
    
    private Integer estimatedHours; // Estimated hours to complete
    
    private Integer actualHours; // Actual hours spent
    
    private TaskPriority priority; // HIGH, MEDIUM, LOW
    
    private LocalDateTime createdAt;
    
    private LocalDateTime startedAt;
    
    private LocalDateTime completedAt;
    
    private String notes; // Additional notes or comments
    
    // Constructor for creating new tasks
    public Task(String id, String title, String assignedTo) {
        this.id = id;
        this.title = title;
        this.assignedTo = assignedTo;
        this.status = TaskStatus.TODO;
        this.priority = TaskPriority.MEDIUM;
        this.createdAt = LocalDateTime.now();
    }
    
    // Helper methods
    public void startTask() {
        this.status = TaskStatus.IN_PROGRESS;
        if (this.startedAt == null) {
            this.startedAt = LocalDateTime.now();
        }
    }
    
    public void completeTask() {
        this.status = TaskStatus.DONE;
        this.completedAt = LocalDateTime.now();
    }
    
    public boolean isOverdue(int maxHours) {
        if (estimatedHours != null && actualHours != null) {
            return actualHours > estimatedHours;
        }
        return false;
    }
    
    public long getDaysInProgress() {
        if (startedAt == null) return 0;
        LocalDateTime end = completedAt != null ? completedAt : LocalDateTime.now();
        return java.time.Duration.between(startedAt, end).toDays();
    }
}
