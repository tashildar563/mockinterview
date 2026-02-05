package com.mockinterview.mockinterview.SprintAI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "standup_updates")
@CompoundIndex(name = "user_date_idx", def = "{'userId': 1, 'date': 1}", unique = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandupUpdate {
    
    @Id
    private String id;
    
    private String userId; // Reference to User
    
    private String sprintId; // Reference to current Sprint
    
    private LocalDate date; // Date of standup
    
    private String yesterday; // What was accomplished yesterday
    
    private String today; // What will be worked on today
    
    private List<String> blockers = new ArrayList<>(); // List of blockers (if any)
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Additional fields for better tracking
    private Integer hoursWorked; // Hours worked yesterday
    
    private List<String> completedStoryIds = new ArrayList<>(); // Story IDs completed
    
    private String mood; // Optional: GOOD, NEUTRAL, STRUGGLING
    
    // Constructor for creating new standup updates
    public StandupUpdate(String userId, String sprintId, LocalDate date, 
                        String yesterday, String today, List<String> blockers) {
        this.userId = userId;
        this.sprintId = sprintId;
        this.date = date;
        this.yesterday = yesterday;
        this.today = today;
        this.blockers = blockers != null ? blockers : new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.completedStoryIds = new ArrayList<>();
    }
    
    // Helper methods
    public boolean hasBlockers() {
        return blockers != null && !blockers.isEmpty();
    }
    
    public void addBlocker(String blocker) {
        if (this.blockers == null) {
            this.blockers = new ArrayList<>();
        }
        if (!this.blockers.contains(blocker)) {
            this.blockers.add(blocker);
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeBlocker(String blocker) {
        if (this.blockers != null) {
            this.blockers.remove(blocker);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addCompletedStory(String storyId) {
        if (this.completedStoryIds == null) {
            this.completedStoryIds = new ArrayList<>();
        }
        if (!this.completedStoryIds.contains(storyId)) {
            this.completedStoryIds.add(storyId);
        }
    }
}
