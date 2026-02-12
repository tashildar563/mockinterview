package com.mockinterview.mockinterview.SprintAI.controller;

import com.mockinterview.mockinterview.SprintAI.requests.AIGenerateRequest;
import com.mockinterview.mockinterview.SprintAI.requests.AISprintPlanningRequest;
import com.mockinterview.mockinterview.SprintAI.response.AIGenerateSprintResponse;
import com.mockinterview.mockinterview.SprintAI.response.AIGenerateStoriesResponse;
import com.mockinterview.mockinterview.SprintAI.response.AISprintPlanningResponse;
import com.mockinterview.mockinterview.SprintAI.services.AIGenerationService;
import com.mockinterview.mockinterview.SprintAI.services.AISprintPlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {
    
    @Autowired
    private AISprintPlanningService aiSprintPlanningService;
    
    @Autowired
    private AIGenerationService aiGenerationService;
    
    /**
     * Generate AI-powered sprint planning suggestion
     * POST /api/ai/sprint-planning
     */
    @PostMapping("/sprint-planning")
    public ResponseEntity<AISprintPlanningResponse> generateSprintPlanningSuggestion(
        @RequestBody AISprintPlanningRequest request
    ) {
        try {
            AISprintPlanningResponse response = aiSprintPlanningService
                .generateSprintPlanningSuggestion(request);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            AISprintPlanningResponse errorResponse = new AISprintPlanningResponse();
            errorResponse.setRecommendation("Failed to generate AI suggestion. Please try again.");
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * Generate user stories with AI
     * POST /api/ai/generate-stories
     */
    @PostMapping("/generate-stories")
    public ResponseEntity<AIGenerateStoriesResponse> generateStories(
        @RequestBody AIGenerateRequest request
    ) {
        try {
            AIGenerateStoriesResponse response = aiGenerationService.generateStories(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new AIGenerateStoriesResponse());
        }
    }
    
    /**
     * Generate full sprint with stories using AI
     * POST /api/ai/generate-sprint
     */
    @PostMapping("/generate-sprint")
    public ResponseEntity<AIGenerateSprintResponse> generateSprint(
        @RequestBody AIGenerateRequest request
    ) {
        try {
            AIGenerateSprintResponse response = aiGenerationService.generateSprint(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new AIGenerateSprintResponse());
        }
    }
}
