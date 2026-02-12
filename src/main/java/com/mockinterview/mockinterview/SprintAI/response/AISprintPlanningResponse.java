package com.mockinterview.mockinterview.SprintAI.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AISprintPlanningResponse {
    
    private String recommendation;
    private List<String> insights;
    private List<String> warnings;
    private List<String> suggestions;
    private List<String> recommendedStories; // Story IDs that AI recommends
}
