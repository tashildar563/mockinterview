package com.mockinterview.mockinterview.SprintAI.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AISprintPlanningRequest {
    
    private String sprintName;
    private String sprintGoal;
    private String teamCapacity;
    private List<StoryInfo> stories;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryInfo {
        private String title;
        private String description;
        private Integer points;
        private String priority;
        private List<String> tags;
    }
}
