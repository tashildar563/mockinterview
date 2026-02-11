package com.mockinterview.mockinterview.SprintAI.controller;


import com.mockinterview.mockinterview.SprintAI.response.AuthResponse;
import com.mockinterview.mockinterview.SprintAI.requests.RegisterRequest;
import com.mockinterview.mockinterview.SprintAI.response.UserResponse;
import com.mockinterview.mockinterview.SprintAI.services.UserService;
import com.mockinterview.mockinterview.SprintAI.entities.User;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow all origins for testing (change in production!)
public class SprintAIController {
    
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
   * Get users (optional search)
   * GET /api/users?search=john
   */
  @GetMapping("/users")
  public ResponseEntity<List<UserResponse>> getUsers(
      @RequestParam(required = false) String search) {

    List<User> users;

    if (search != null && !search.trim().isEmpty()) {
      users = userService.searchUsers(search);
    } else {
      users = userService.getAllUsers();
    }

    List<UserResponse> response = users.stream()
        .map(user -> new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail()
        ))
        .toList();

    return ResponseEntity.ok(response);
  }

}
