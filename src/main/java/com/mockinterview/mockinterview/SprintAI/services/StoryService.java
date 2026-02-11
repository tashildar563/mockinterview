package com.mockinterview.mockinterview.SprintAI.services;

import com.mockinterview.mockinterview.SprintAI.dto.TaskRequest;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import com.mockinterview.mockinterview.SprintAI.entities.Task;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.enums.TaskStatus;
import com.mockinterview.mockinterview.SprintAI.repositories.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StoryService {
    
    @Autowired
    private StoryRepository storyRepository;
    
    /**
     * Create a new story
     */
    public Story createStory(Story story) {
        story.setCreatedAt(LocalDateTime.now());
        story.setStatus(StoryStatus.TODO);
        return storyRepository.save(story);
    }
    
    /**
     * Get all stories
     */
    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }
    
    /**
     * Get story by ID
     */
    public Optional<Story> getStoryById(String id) {
        return storyRepository.findById(id);
    }
    
    /**
     * Get stories by sprint
     */
    public List<Story> getStoriesBySprintId(String sprintId) {
        return storyRepository.findBySprintId(sprintId);
    }
    
    /**
     * Get stories by status
     */
    public List<Story> getStoriesByStatus(String sprintId, StoryStatus status) {
        return storyRepository.findBySprintIdAndStatus(sprintId, status);
    }
    
    /**
     * Get stories assigned to a user
     */
    public List<Story> getStoriesAssignedToUser(String userId) {
        return storyRepository.findByAssignedTo(userId);
    }
    
    /**
     * Update story
     */
    public Story updateStory(String id, Story updatedStory) {
        Story story = storyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + id));
        
        story.setTitle(updatedStory.getTitle());
        story.setDescription(updatedStory.getDescription());
        story.setPoints(updatedStory.getPoints());
        story.setPriority(updatedStory.getPriority());
        story.setAcceptanceCriteria(updatedStory.getAcceptanceCriteria());
        story.setTags(updatedStory.getTags());
        
        return storyRepository.save(story);
    }
    
    /**
     * Move story to different status
     */
    public Story moveStory(String id, StoryStatus newStatus) {
        Story story = storyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + id));
        
        story.setStatus(newStatus);
        
        if (newStatus == StoryStatus.IN_PROGRESS && story.getStartedAt() == null) {
            story.setStartedAt(LocalDateTime.now());
        }
        
        if (newStatus == StoryStatus.DONE) {
            story.setCompletedAt(LocalDateTime.now());
        }
        
        return storyRepository.save(story);
    }
    
    /**
     * Delete story
     */
    public void deleteStory(String id) {
        if (!storyRepository.existsById(id)) {
            throw new RuntimeException("Story not found with id: " + id);
        }
        storyRepository.deleteById(id);
    }
    
    // ==================== TASK MANAGEMENT ====================
    
    /**
     * Add a task to a story
     */
    public Story addTaskToStory(String storyId, TaskRequest taskRequest) {
        Story story = storyRepository.findById(storyId)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + storyId));
        
        // Create new task
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setAssignedTo(taskRequest.getAssignedTo());
        task.setEstimatedHours(taskRequest.getEstimatedHours());
        task.setPriority(taskRequest.getPriority());
        task.setNotes(taskRequest.getNotes());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        
        // Add task to story
        story.addTask(task);
        
        return storyRepository.save(story);
    }
    
    /**
     * Update a task within a story
     */
    public Story updateTask(String storyId, String taskId, TaskRequest taskRequest) {
        Story story = storyRepository.findById(storyId)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + storyId));
        
        // Find and update task
        Task existingTask = story.getTasks().stream()
            .filter(t -> t.getId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setAssignedTo(taskRequest.getAssignedTo());
        existingTask.setEstimatedHours(taskRequest.getEstimatedHours());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setNotes(taskRequest.getNotes());
        
        return storyRepository.save(story);
    }
    
    /**
     * Move task to different status
     */
    public Story moveTask(String storyId, String taskId, TaskStatus newStatus) {
        Story story = storyRepository.findById(storyId)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + storyId));
        
        Task task = story.getTasks().stream()
            .filter(t -> t.getId().equals(taskId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        task.setStatus(newStatus);
        
        if (newStatus == TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
            task.setStartedAt(LocalDateTime.now());
        }
        
        if (newStatus == TaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
        }
        
        // Auto-update story status if all tasks are done
        if (story.areAllTasksCompleted()) {
            story.setStatus(StoryStatus.IN_REVIEW);
        }
        
        return storyRepository.save(story);
    }
    
    /**
     * Delete a task from a story
     */
    public Story deleteTask(String storyId, String taskId) {
        Story story = storyRepository.findById(storyId)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + storyId));
        
        story.removeTask(taskId);
        
        return storyRepository.save(story);
    }
    
    /**
     * Get all tasks for a specific user across all stories
     */
    public List<Task> getTasksForUser(String userId) {
        List<Story> allStories = storyRepository.findAll();
        
        return allStories.stream()
            .flatMap(story -> story.getTasks().stream())
            .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().equals(userId))
            .toList();
    }
    
    /**
     * Get tasks by status for a user
     */
    public List<Task> getTasksByStatusForUser(String userId, TaskStatus status) {
        return getTasksForUser(userId).stream()
            .filter(task -> task.getStatus() == status)
            .toList();
    }
}
