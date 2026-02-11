package com.mockinterview.mockinterview.SprintAI.dto;

import com.mockinterview.mockinterview.SprintAI.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    
    @NotBlank(message = "Task title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Assigned user is required")
    private String assignedTo; // User ID
    
    private Integer estimatedHours;
    
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    private String notes;
}
