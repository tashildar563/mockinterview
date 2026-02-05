package com.mockinterview.mockinterview.SprintAI.entities;

import com.mockinterview.mockinterview.SprintAI.enums.StoryPriority;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Story {
    
    @Id
    private String id;
    
    private String sprintId; // Reference to Sprint
    
    private String title;
    
    private String description;
    
    private Integer points; // Story points (1, 2, 3, 5, 8, 13, etc.)
    
    private StoryStatus status;
    
    private String assignedTo; // User ID of assigned developer
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private LocalDateTime startedAt; // When moved to IN_PROGRESS
    
    private LocalDateTime completedAt; // When moved to DONE
    
    // Additional fields for better tracking
    private List<String> blockers = new ArrayList<>(); // List of blocker descriptions
    
    private StoryPriority priority;
    
    private String acceptanceCriteria; // Definition of Done
    
    private List<String> tags = new ArrayList<>(); // e.g., ["backend", "security", "api"]
    
    // Constructor for creating new stories
    public Story(String sprintId, String title, String description, 
                 Integer points, String assignedTo) {
        this.sprintId = sprintId;
        this.title = title;
        this.description = description;
        this.points = points;
        this.assignedTo = assignedTo;
        this.status = StoryStatus.TODO;
        this.priority = StoryPriority.MEDIUM;
        this.createdAt = LocalDateTime.now();
        this.blockers = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    // Helper methods
    public void moveToInProgress() {
        this.status = StoryStatus.IN_PROGRESS;
        if (this.startedAt == null) {
            this.startedAt = LocalDateTime.now();
        }
    }
    
    public void moveToCompleted() {
        this.status = StoryStatus.DONE;
        this.completedAt = LocalDateTime.now();
        this.blockers.clear(); // Clear blockers when done
    }
    
    public void markAsBlocked(String blockerDescription) {
        this.status = StoryStatus.BLOCKED;
        if (!this.blockers.contains(blockerDescription)) {
            this.blockers.add(blockerDescription);
        }
    }
    
    public void removeBlocker(String blockerDescription) {
        this.blockers.remove(blockerDescription);
        if (this.blockers.isEmpty() && this.status == StoryStatus.BLOCKED) {
            this.status = StoryStatus.IN_PROGRESS;
        }
    }
    
    public boolean isBlocked() {
        return !blockers.isEmpty();
    }
    
    public long getDaysInProgress() {
        if (startedAt == null) return 0;
        LocalDateTime end = completedAt != null ? completedAt : LocalDateTime.now();
        return java.time.Duration.between(startedAt, end).toDays();
    }

  public StoryStatus getStatus() {
      return status;
  }

  public int getPoints() {
      return points;
  }
}
