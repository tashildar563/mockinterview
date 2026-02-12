package com.mockinterview.mockinterview.SprintAI.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIGenerateRequest {
    
    private String prompt;
    private ContextInfo context;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextInfo {
        private String projectType;
        private String techStack;
        private String teamSize;
        private String sprintDuration;
        private String additionalInfo;
    }
}
