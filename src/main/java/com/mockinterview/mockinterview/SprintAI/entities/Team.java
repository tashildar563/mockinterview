package com.mockinterview.mockinterview.SprintAI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    
    @Id
    private String id;
    
    private String name;
    
    private String description;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private List<String> memberIds = new ArrayList<>(); // List of User IDs
    
    private String scrumMasterId; // Reference to User who is Scrum Master
    
    private boolean active = true;
    
    // Constructor for creating new teams
    public Team(String name, String description, String scrumMasterId) {
        this.name = name;
        this.description = description;
        this.scrumMasterId = scrumMasterId;
        this.createdAt = LocalDateTime.now();
        this.memberIds = new ArrayList<>();
        this.active = true;
    }
    
    // Helper methods
    public void addMember(String userId) {
        if (!this.memberIds.contains(userId)) {
            this.memberIds.add(userId);
        }
    }
    
    public void removeMember(String userId) {
        this.memberIds.remove(userId);
    }
}
