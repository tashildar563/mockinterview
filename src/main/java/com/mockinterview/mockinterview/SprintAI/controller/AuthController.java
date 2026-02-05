package com.mockinterview.mockinterview.SprintAI.controller;


import com.mockinterview.mockinterview.SprintAI.AuthResponse;
import com.mockinterview.mockinterview.SprintAI.RegisterRequest;
import com.mockinterview.mockinterview.SprintAI.UserService;
import com.mockinterview.mockinterview.SprintAI.entities.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow all origins for testing (change in production!)
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Register a new user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            // Register user
            User user = userService.registerUser(request);
            
            // Create response
            AuthResponse response = new AuthResponse(
                "",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getTeamId()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Test endpoint to check if API is working
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "SprintAI API is running! 🚀");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
