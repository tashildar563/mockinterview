package com.mockinterview.mockinterview.SprintAI.entities;

import com.mockinterview.mockinterview.SprintAI.entities.Task;
import com.mockinterview.mockinterview.SprintAI.enums.StoryPriority;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.enums.TaskStatus;
import com.mockinterview.mockinterview.SprintAI.entities.*;
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

  private String assignedTo; // User ID of assigned developer (story owner)

  @CreatedDate
  private LocalDateTime createdAt;

  private LocalDateTime startedAt; // When moved to IN_PROGRESS

  private LocalDateTime completedAt; // When moved to DONE

  // Additional fields for better tracking
  private List<String> blockers = new ArrayList<>(); // List of blocker descriptions

  private StoryPriority priority;

  private String acceptanceCriteria; // Definition of Done

  private List<String> tags = new ArrayList<>(); // e.g., ["backend", "security", "api"]

  // NEW: Tasks within the story (assignable to individual team members)
  private List<Task> tasks = new ArrayList<>();

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
    this.tasks = new ArrayList<>();
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

  // NEW: Task management methods
  public void addTask(Task task) {
    if (this.tasks == null) {
      this.tasks = new ArrayList<>();
    }
    this.tasks.add(task);
  }

  public void removeTask(String taskId) {
    if (this.tasks != null) {
      this.tasks.removeIf(task -> task.getId().equals(taskId));
    }
  }

  public void updateTask(String taskId, Task updatedTask) {
    if (this.tasks != null) {
      for (int i = 0; i < this.tasks.size(); i++) {
        if (this.tasks.get(i).getId().equals(taskId)) {
          this.tasks.set(i, updatedTask);
          break;
        }
      }
    }
  }

  // Calculate story progress based on completed tasks
  public int getTaskCompletionPercentage() {
    if (tasks == null || tasks.isEmpty()) {
      return 0;
    }
    long completedTasks = tasks.stream()
        .filter(task -> task.getStatus() == TaskStatus.DONE)
        .count();
    return (int) ((completedTasks * 100.0) / tasks.size());
  }

  // Check if all tasks are completed
  public boolean areAllTasksCompleted() {
    if (tasks == null || tasks.isEmpty()) {
      return false;
    }
    return tasks.stream().allMatch(task -> task.getStatus() == TaskStatus.DONE);
  }

  // Get tasks by assignee
  public List<Task> getTasksByAssignee(String userId) {
    if (tasks == null) {
      return new ArrayList<>();
    }
    return tasks.stream()
        .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().equals(userId))
        .toList();
  }
}