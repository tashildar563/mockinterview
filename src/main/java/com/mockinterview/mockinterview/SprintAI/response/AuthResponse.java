package com.mockinterview.mockinterview.SprintAI.response;


import com.mockinterview.mockinterview.SprintAI.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private String id;
    private String name;
    private String email;
    private UserRole role;
    private String teamId;
    
    // Constructor without type (defaults to "Bearer")
    public AuthResponse(String token, String id, String name, String email, UserRole role, String teamId) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.teamId = teamId;
    }
}
