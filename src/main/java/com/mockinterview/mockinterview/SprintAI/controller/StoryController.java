package com.mockinterview.mockinterview.SprintAI.controller;
import com.mockinterview.mockinterview.SprintAI.dto.TaskRequest;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import com.mockinterview.mockinterview.SprintAI.entities.Task;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.enums.TaskStatus;
import com.mockinterview.mockinterview.SprintAI.services.StoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stories")
@CrossOrigin(origins = "*")
public class StoryController {
    
    @Autowired
    private StoryService storyService;
    
    /**
     * Get all stories
     * GET /api/stories
     */
    @GetMapping
    public ResponseEntity<List<Story>> getAllStories() {
        return ResponseEntity.ok(storyService.getAllStories());
    }
    
    /**
     * Get story by ID
     * GET /api/stories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoryById(@PathVariable String id) {
        try {
            Story story = storyService.getStoryById(id)
                .orElseThrow(() -> new RuntimeException("Story not found"));
            return ResponseEntity.ok(story);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get stories by sprint
     * GET /api/stories/sprint/{sprintId}
     */
    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<Story>> getStoriesBySprintId(@PathVariable String sprintId) {
        return ResponseEntity.ok(storyService.getStoriesBySprintId(sprintId));
    }
    
    /**
     * Get stories assigned to a user
     * GET /api/stories/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Story>> getStoriesAssignedToUser(@PathVariable String userId) {
        return ResponseEntity.ok(storyService.getStoriesAssignedToUser(userId));
    }
    
    /**
     * Create a new story
     * POST /api/stories
     */
    @PostMapping
    public ResponseEntity<?> createStory(@Valid @RequestBody Story story) {
        try {
            Story createdStory = storyService.createStory(story);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStory);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Update a story
     * PUT /api/stories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStory(@PathVariable String id, @RequestBody Story story) {
        try {
            Story updatedStory = storyService.updateStory(id, story);
            return ResponseEntity.ok(updatedStory);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Move story to different status
     * PUT /api/stories/{id}/move
     */
    @PutMapping("/{id}/move")
    public ResponseEntity<?> moveStory(
        @PathVariable String id,
        @RequestParam StoryStatus status
    ) {
        try {
            Story movedStory = storyService.moveStory(id, status);
            return ResponseEntity.ok(movedStory);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Delete a story
     * DELETE /api/stories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStory(@PathVariable String id) {
        try {
            storyService.deleteStory(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Story deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // ==================== TASK ENDPOINTS ====================
    
    /**
     * Add a task to a story
     * POST /api/stories/{storyId}/tasks
     */
    @PostMapping("/{storyId}/tasks")
    public ResponseEntity<?> addTask(
        @PathVariable String storyId,
        @Valid @RequestBody TaskRequest taskRequest
    ) {
        try {
            Story story = storyService.addTaskToStory(storyId, taskRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(story);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Update a task
     * PUT /api/stories/{storyId}/tasks/{taskId}
     */
    @PutMapping("/{storyId}/tasks/{taskId}")
    public ResponseEntity<?> updateTask(
        @PathVariable String storyId,
        @PathVariable String taskId,
        @Valid @RequestBody TaskRequest taskRequest
    ) {
        try {
            Story story = storyService.updateTask(storyId, taskId, taskRequest);
            return ResponseEntity.ok(story);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Move task to different status
     * PUT /api/stories/{storyId}/tasks/{taskId}/move
     */
    @PutMapping("/{storyId}/tasks/{taskId}/move")
    public ResponseEntity<?> moveTask(
        @PathVariable String storyId,
        @PathVariable String taskId,
        @RequestParam TaskStatus status
    ) {
        try {
            Story story = storyService.moveTask(storyId, taskId, status);
            return ResponseEntity.ok(story);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Delete a task
     * DELETE /api/stories/{storyId}/tasks/{taskId}
     */
    @DeleteMapping("/{storyId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(
        @PathVariable String storyId,
        @PathVariable String taskId
    ) {
        try {
            Story story = storyService.deleteTask(storyId, taskId);
            return ResponseEntity.ok(story);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get all tasks for a user
     * GET /api/stories/tasks/user/{userId}
     */
    @GetMapping("/tasks/user/{userId}")
    public ResponseEntity<List<Task>> getTasksForUser(@PathVariable String userId) {
        return ResponseEntity.ok(storyService.getTasksForUser(userId));
    }
    
    /**
     * Get tasks by status for a user
     * GET /api/stories/tasks/user/{userId}/status/{status}
     */
    @GetMapping("/tasks/user/{userId}/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatusForUser(
        @PathVariable String userId,
        @PathVariable TaskStatus status
    ) {
        return ResponseEntity.ok(storyService.getTasksByStatusForUser(userId, status));
    }
}
