package com.mockinterview.mockinterview.SprintAI.entities;

import com.mockinterview.mockinterview.SprintAI.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    private String id;
    
    private String name;
    
    @Indexed(unique = true)
    private String email;
    
    private String password; // Store hashed password (BCrypt)
    
    private UserRole role;
    
    private String teamId; // Reference to Team collection
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private boolean active = true;
    
    // Constructor without id (for creating new users)
    public User(String name, String email, String password, UserRole role, String teamId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.teamId = teamId;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
}
