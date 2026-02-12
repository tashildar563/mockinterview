package com.mockinterview.mockinterview.SprintAI.services;

import com.google.genai.*;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.mockinterview.mockinterview.SprintAI.requests.AISprintPlanningRequest;
import com.mockinterview.mockinterview.SprintAI.response.AISprintPlanningResponse;
import com.mockinterview.mockinterview.gemini.GeminiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AISprintPlanningService {
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    /**
     * Generate AI-powered sprint planning suggestion using Gemini
     */
    public AISprintPlanningResponse generateSprintPlanningSuggestion(AISprintPlanningRequest request) {
        try {
            // Initialize Gemini model
          GeminiClient client = GeminiClient.getInstance();
            
            // Build prompt for Gemini
            String prompt = buildSprintPlanningPrompt(request);
            
            GenerateContentResponse response = client.generateText(prompt);
            
            // Parse AI response
            String aiResponse = response.text();
            
            // Extract structured information from AI response
            return parseAIResponse(aiResponse, request);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            // Fallback response
            AISprintPlanningResponse fallback = new AISprintPlanningResponse();
            fallback.setRecommendation("Unable to generate AI suggestion. Please try again.");
            fallback.setInsights(new ArrayList<>());
            fallback.setWarnings(List.of("AI service is temporarily unavailable"));
            return fallback;
        }
    }
    
    /**
     * Build comprehensive prompt for Gemini
     */
    private String buildSprintPlanningPrompt(AISprintPlanningRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are an expert Scrum Master and Agile coach. ");
        prompt.append("Help me plan a sprint based on the following information:\n\n");
        
        // Sprint Details
        prompt.append("SPRINT DETAILS:\n");
        prompt.append("- Sprint Name: ").append(request.getSprintName()).append("\n");
        prompt.append("- Sprint Goal: ").append(request.getSprintGoal()).append("\n");
        
        if (request.getTeamCapacity() != null && !request.getTeamCapacity().isEmpty()) {
            prompt.append("- Team Capacity: ").append(request.getTeamCapacity()).append(" story points\n");
        }
        
        prompt.append("\n");
        
        // Stories being considered
        prompt.append("STORIES BEING CONSIDERED:\n");
        int totalPoints = 0;
        for (int i = 0; i < request.getStories().size(); i++) {
            var story = request.getStories().get(i);
            prompt.append((i + 1)).append(". ");
            prompt.append(story.getTitle());
            prompt.append(" (").append(story.getPoints()).append(" points, Priority: ").append(story.getPriority()).append(")\n");
            prompt.append("   Description: ").append(story.getDescription()).append("\n");
            
            if (story.getTags() != null && !story.getTags().isEmpty()) {
                prompt.append("   Tags: ").append(String.join(", ", story.getTags())).append("\n");
            }
            
            totalPoints += story.getPoints();
        }
        
        prompt.append("\nTotal Story Points Selected: ").append(totalPoints).append("\n\n");
        
        // Ask for specific analysis
        prompt.append("Please provide:\n");
        prompt.append("1. RECOMMENDATION: A clear recommendation on whether these stories are suitable for the sprint\n");
        prompt.append("2. INSIGHTS: 3-5 key insights about the selected stories (dependencies, risks, complexity, etc.)\n");
        prompt.append("3. WARNINGS: Any potential issues or concerns (overcommitment, missing priorities, etc.)\n");
        prompt.append("4. SUGGESTIONS: Specific suggestions for improvement\n\n");
        
        prompt.append("Format your response as:\n");
        prompt.append("RECOMMENDATION: [your recommendation here]\n");
        prompt.append("INSIGHTS:\n- [insight 1]\n- [insight 2]\n- [insight 3]\n");
        prompt.append("WARNINGS:\n- [warning 1]\n- [warning 2]\n");
        prompt.append("SUGGESTIONS:\n- [suggestion 1]\n- [suggestion 2]\n");
        
        return prompt.toString();
    }
    
    /**
     * Parse AI response into structured format
     */
    private AISprintPlanningResponse parseAIResponse(String aiResponse, AISprintPlanningRequest request) {
        AISprintPlanningResponse response = new AISprintPlanningResponse();
        
        try {
            // Extract sections
            String recommendation = extractSection(aiResponse, "RECOMMENDATION:", "INSIGHTS:");
            List<String> insights = extractList(aiResponse, "INSIGHTS:", "WARNINGS:");
            List<String> warnings = extractList(aiResponse, "WARNINGS:", "SUGGESTIONS:");
            List<String> suggestions = extractList(aiResponse, "SUGGESTIONS:", null);
            
            response.setRecommendation(recommendation.trim());
            response.setInsights(insights);
            response.setWarnings(warnings);
            response.setSuggestions(suggestions);
            
            // Add story IDs that are recommended
            response.setRecommendedStories(request.getStories().stream()
                .map(s -> s.getTitle()) // In real implementation, use story IDs
                .toList());
            
        } catch (Exception e) {
            // If parsing fails, return the raw response
            response.setRecommendation(aiResponse);
            response.setInsights(new ArrayList<>());
            response.setWarnings(new ArrayList<>());
            response.setSuggestions(new ArrayList<>());
        }
        
        return response;
    }
    
    /**
     * Extract section from AI response
     */
    private String extractSection(String text, String startMarker, String endMarker) {
        try {
            int start = text.indexOf(startMarker);
            if (start == -1) return "";
            
            start += startMarker.length();
            
            int end = endMarker != null ? text.indexOf(endMarker, start) : text.length();
            if (end == -1) end = text.length();
            
            return text.substring(start, end).trim();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Extract list items from AI response
     */
    private List<String> extractList(String text, String startMarker, String endMarker) {
        List<String> items = new ArrayList<>();
        
        try {
            String section = extractSection(text, startMarker, endMarker);
            String[] lines = section.split("\n");
            
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("-") || line.startsWith("•") || line.matches("^\\d+\\..*")) {
                    // Remove list markers
                    line = line.replaceFirst("^[-•]\\s*", "");
                    line = line.replaceFirst("^\\d+\\.\\s*", "");
                    if (!line.isEmpty()) {
                        items.add(line.trim());
                    }
                }
            }
        } catch (Exception e) {
            // Return empty list on error
        }
        
        return items;
    }
}
