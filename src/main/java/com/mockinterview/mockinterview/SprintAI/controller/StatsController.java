package com.mockinterview.mockinterview.SprintAI.controller;

import com.mockinterview.mockinterview.SprintAI.response.DashboardStatsResponse;
import com.mockinterview.mockinterview.SprintAI.response.SprintStatsResponse;
import com.mockinterview.mockinterview.SprintAI.services.StatsService;
import com.mockinterview.mockinterview.SprintAI.response.UserStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {
    
    @Autowired
    private StatsService statsService;
    
    /**
     * Get overall dashboard statistics
     * GET /api/stats/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(statsService.getDashboardStats());
    }
    
    /**
     * Get statistics for a specific sprint
     * GET /api/stats/sprint/{sprintId}
     */
    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<?> getSprintStats(@PathVariable String sprintId) {
        try {
            SprintStatsResponse stats = statsService.getSprintStats(sprintId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get statistics for a specific user
     * GET /api/stats/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserStats(@PathVariable String userId) {
        try {
            UserStatsResponse stats = statsService.getUserStats(userId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get quick counts (fast API for simple counters)
     * GET /api/stats/counts
     */
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Long>> getQuickCounts() {
        return ResponseEntity.ok(statsService.getQuickCounts());
    }
    
    /**
     * Get count of entities by type
     * GET /api/stats/count/{entityType}
     * 
     * Supported types: sprints, stories, users, teams
     */
    @GetMapping("/count/{entityType}")
    public ResponseEntity<?> getCountByType(@PathVariable String entityType) {
        try {
            Map<String, Long> counts = statsService.getQuickCounts();
            Long count;
            
            switch (entityType.toLowerCase()) {
                case "sprints":
                    count = counts.get("totalSprints");
                    break;
                case "stories":
                    count = counts.get("totalStories");
                    break;
                case "users":
                    count = counts.get("totalUsers");
                    break;
                case "teams":
                    count = counts.get("totalTeams");
                    break;
                case "active-sprints":
                    count = counts.get("activeSprints");
                    break;
                case "done-stories":
                    count = counts.get("doneStories");
                    break;
                default:
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Invalid entity type. Use: sprints, stories, users, teams, active-sprints, done-stories");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", entityType);
            response.put("count", count);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
