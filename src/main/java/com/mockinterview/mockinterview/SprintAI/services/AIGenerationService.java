package com.mockinterview.mockinterview.SprintAI.services;

import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mockinterview.mockinterview.SprintAI.entities.Sprint;
import com.mockinterview.mockinterview.SprintAI.entities.Story;
import com.mockinterview.mockinterview.SprintAI.enums.SprintStatus;
import com.mockinterview.mockinterview.SprintAI.enums.StoryPriority;
import com.mockinterview.mockinterview.SprintAI.enums.StoryStatus;
import com.mockinterview.mockinterview.SprintAI.requests.AIGenerateRequest;
import com.mockinterview.mockinterview.SprintAI.response.AIGenerateSprintResponse;
import com.mockinterview.mockinterview.SprintAI.response.AIGenerateStoriesResponse;
import com.mockinterview.mockinterview.gemini.GeminiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIGenerationService {

  private final Gson gson = new Gson();
  @Value("${gemini.api.key}")
  private String geminiApiKey;

  /**
   * Generate user stories using Gemini AI
   */
  public AIGenerateStoriesResponse generateStories(AIGenerateRequest request) {
    try {
      GeminiClient geminiClient = GeminiClient.getInstance();

      String prompt = buildStoriesPrompt(request);

      GenerateContentResponse response = geminiClient.generateText(prompt);
      String aiResponse = response.text();

      return parseStoriesResponse(aiResponse);

    } catch (Exception e) {
      e.printStackTrace();
      return createFallbackStoriesResponse();
    }
  }

  /**
   * Generate full sprint with stories using Gemini AI
   */
  public AIGenerateSprintResponse generateSprint(AIGenerateRequest request) {
    try {
      GeminiClient geminiClient = GeminiClient.getInstance();

      String prompt = buildSprintPrompt(request);

      GenerateContentResponse response = geminiClient.generateText(prompt);
      String aiResponse = response.text();

      return parseSprintResponse(aiResponse, request);

    } catch (Exception e) {
      e.printStackTrace();
      return createFallbackSprintResponse();
    }
  }

  /**
   * Build prompt for generating stories
   */
  private String buildStoriesPrompt(AIGenerateRequest request) {
    StringBuilder prompt = new StringBuilder();

    prompt.append("You are an expert Agile Product Owner and User Story writer. ");
    prompt.append("Generate detailed user stories based on this requirement:\n\n");
    prompt.append("REQUIREMENT: ").append(request.getPrompt()).append("\n\n");

    if (request.getContext() != null) {
      var ctx = request.getContext();
      if (ctx.getProjectType() != null && !ctx.getProjectType().isEmpty()) {
        prompt.append("PROJECT TYPE: ").append(ctx.getProjectType()).append("\n");
      }
      if (ctx.getTechStack() != null && !ctx.getTechStack().isEmpty()) {
        prompt.append("TECH STACK: ").append(ctx.getTechStack()).append("\n");
      }
      if (ctx.getAdditionalInfo() != null && !ctx.getAdditionalInfo().isEmpty()) {
        prompt.append("ADDITIONAL INFO: ").append(ctx.getAdditionalInfo()).append("\n");
      }
    }

    prompt.append("\nGenerate 5-8 user stories in JSON format. Each story should have:\n");
    prompt.append("- title: Clear, concise story title\n");
    prompt.append(
        "- description: Detailed description in \"As a [user], I want [feature] so that [benefit]\" format\n");
    prompt.append("- points: Story points (1, 2, 3, 5, 8, 13) based on complexity\n");
    prompt.append("- priority: HIGH, MEDIUM, or LOW\n");
    prompt.append("- acceptanceCriteria: Clear acceptance criteria (bullet points)\n");
    prompt.append(
        "- tags: Array of relevant tags (e.g., [\"frontend\", \"backend\", \"api\"])\n\n");

    prompt.append("Return ONLY valid JSON in this exact format (no markdown, no code blocks):\n");
    prompt.append("{\n");
    prompt.append("  \"stories\": [\n");
    prompt.append("    {\n");
    prompt.append("      \"title\": \"...\",\n");
    prompt.append("      \"description\": \"...\",\n");
    prompt.append("      \"points\": 5,\n");
    prompt.append("      \"priority\": \"HIGH\",\n");
    prompt.append("      \"acceptanceCriteria\": \"...\",\n");
    prompt.append("      \"tags\": [\"...\"]\n");
    prompt.append("    }\n");
    prompt.append("  ]\n");
    prompt.append("}\n");

    return prompt.toString();
  }

  /**
   * Build prompt for generating sprint
   */
  private String buildSprintPrompt(AIGenerateRequest request) {
    StringBuilder prompt = new StringBuilder();

    prompt.append("You are an expert Scrum Master and Sprint Planner. ");
    prompt.append("Generate a complete sprint plan based on this requirement:\n\n");
    prompt.append("REQUIREMENT: ").append(request.getPrompt()).append("\n\n");

    if (request.getContext() != null) {
      var ctx = request.getContext();
      if (ctx.getProjectType() != null && !ctx.getProjectType().isEmpty()) {
        prompt.append("PROJECT TYPE: ").append(ctx.getProjectType()).append("\n");
      }
      if (ctx.getTechStack() != null && !ctx.getTechStack().isEmpty()) {
        prompt.append("TECH STACK: ").append(ctx.getTechStack()).append("\n");
      }
      if (ctx.getSprintDuration() != null && !ctx.getSprintDuration().isEmpty()) {
        prompt.append("SPRINT DURATION: ").append(ctx.getSprintDuration()).append(" weeks\n");
      }
      if (ctx.getTeamSize() != null && !ctx.getTeamSize().isEmpty()) {
        prompt.append("TEAM SIZE: ").append(ctx.getTeamSize()).append(" developers\n");
      }
    }

    prompt.append("\nGenerate a sprint plan with:\n");
    prompt.append("1. Sprint details (name, goal, dates)\n");
    prompt.append("2. 5-10 user stories that fit in the sprint\n\n");

    prompt.append("Return ONLY valid JSON in this exact format (no markdown, no code blocks):\n");
    prompt.append("{\n");
    prompt.append("  \"sprint\": {\n");
    prompt.append("    \"name\": \"Sprint X - [Focus Area]\",\n");
    prompt.append("    \"goal\": \"Clear, measurable sprint goal\"\n");
    prompt.append("  },\n");
    prompt.append("  \"stories\": [\n");
    prompt.append("    {\n");
    prompt.append("      \"title\": \"...\",\n");
    prompt.append("      \"description\": \"As a [user], I want [feature] so that [benefit]\",\n");
    prompt.append("      \"points\": 5,\n");
    prompt.append("      \"priority\": \"HIGH\",\n");
    prompt.append("      \"acceptanceCriteria\": \"Criteria here\",\n");
    prompt.append("      \"tags\": [\"backend\", \"api\"]\n");
    prompt.append("    }\n");
    prompt.append("  ]\n");
    prompt.append("}\n");

    return prompt.toString();
  }

  /**
   * Parse AI response for stories
   */
  private AIGenerateStoriesResponse parseStoriesResponse(String aiResponse) {
    try {
      // Clean the response - remove markdown code blocks if present
      String cleanedResponse = aiResponse
          .replaceAll("```json\\s*", "")
          .replaceAll("```\\s*", "")
          .trim();

      JsonObject jsonResponse = gson.fromJson(cleanedResponse, JsonObject.class);
      JsonArray storiesArray = jsonResponse.getAsJsonArray("stories");

      List<Story> stories = new ArrayList<>();

      for (JsonElement storyElement : storiesArray) {
        JsonObject storyObj = storyElement.getAsJsonObject();

        Story story = new Story();
        story.setTitle(storyObj.get("title").getAsString());
        story.setDescription(storyObj.get("description").getAsString());
        story.setPoints(storyObj.get("points").getAsInt());
        story.setPriority(StoryPriority.valueOf(storyObj.get("priority").getAsString()));
        story.setStatus(StoryStatus.TODO);

        if (storyObj.has("acceptanceCriteria")) {
          story.setAcceptanceCriteria(storyObj.get("acceptanceCriteria").getAsString());
        }

        if (storyObj.has("tags")) {
          JsonArray tagsArray = storyObj.getAsJsonArray("tags");
          List<String> tags = new ArrayList<>();
          for (JsonElement tagElement : tagsArray) {
            tags.add(tagElement.getAsString());
          }
          story.setTags(tags);
        }

        story.setBlockers(new ArrayList<>());
        story.setTasks(new ArrayList<>());

        stories.add(story);
      }

      AIGenerateStoriesResponse response = new AIGenerateStoriesResponse();
      response.setStories(stories);
      return response;

    } catch (Exception e) {
      e.printStackTrace();
      return createFallbackStoriesResponse();
    }
  }

  /**
   * Parse AI response for sprint
   */
  private AIGenerateSprintResponse parseSprintResponse(String aiResponse,
      AIGenerateRequest request) {
    try {
      String cleanedResponse = aiResponse
          .replaceAll("```json\\s*", "")
          .replaceAll("```\\s*", "")
          .trim();

      JsonObject jsonResponse = gson.fromJson(cleanedResponse, JsonObject.class);

      // Parse Sprint
      JsonObject sprintObj = jsonResponse.getAsJsonObject("sprint");
      Sprint sprint = new Sprint();
      sprint.setName(sprintObj.get("name").getAsString());
      sprint.setGoal(sprintObj.get("goal").getAsString());
      sprint.setStatus(SprintStatus.PLANNED);

      // Calculate dates based on duration
      LocalDate startDate = LocalDate.now();
      int duration = 2; // default
      if (request.getContext() != null && request.getContext().getSprintDuration() != null) {
        try {
          duration = Integer.parseInt(request.getContext().getSprintDuration());
        } catch (NumberFormatException e) {
          duration = 2;
        }
      }
      LocalDate endDate = startDate.plusWeeks(duration);

      sprint.setStartDate(startDate);
      sprint.setEndDate(endDate);

      // Parse Stories
      JsonArray storiesArray = jsonResponse.getAsJsonArray("stories");
      List<Story> stories = new ArrayList<>();

      for (JsonElement storyElement : storiesArray) {
        JsonObject storyObj = storyElement.getAsJsonObject();

        Story story = new Story();
        story.setTitle(storyObj.get("title").getAsString());
        story.setDescription(storyObj.get("description").getAsString());
        story.setPoints(storyObj.get("points").getAsInt());
        story.setPriority(StoryPriority.valueOf(storyObj.get("priority").getAsString()));
        story.setStatus(StoryStatus.TODO);

        if (storyObj.has("acceptanceCriteria")) {
          story.setAcceptanceCriteria(storyObj.get("acceptanceCriteria").getAsString());
        }

        if (storyObj.has("tags")) {
          JsonArray tagsArray = storyObj.getAsJsonArray("tags");
          List<String> tags = new ArrayList<>();
          for (JsonElement tagElement : tagsArray) {
            tags.add(tagElement.getAsString());
          }
          story.setTags(tags);
        }

        story.setBlockers(new ArrayList<>());
        story.setTasks(new ArrayList<>());

        stories.add(story);
      }

      AIGenerateSprintResponse response = new AIGenerateSprintResponse();
      response.setSprint(sprint);
      response.setStories(stories);
      return response;

    } catch (Exception e) {
      e.printStackTrace();
      return createFallbackSprintResponse();
    }
  }

  /**
   * Create fallback response for stories
   */
  private AIGenerateStoriesResponse createFallbackStoriesResponse() {
    AIGenerateStoriesResponse response = new AIGenerateStoriesResponse();

    Story fallbackStory = new Story();
    fallbackStory.setTitle("AI Generation Failed");
    fallbackStory.setDescription(
        "Unable to generate stories. Please try again or create manually.");
    fallbackStory.setPoints(0);
    fallbackStory.setPriority(StoryPriority.MEDIUM);
    fallbackStory.setStatus(StoryStatus.TODO);
    fallbackStory.setTags(List.of("ai-error"));

    response.setStories(List.of(fallbackStory));
    return response;
  }

  /**
   * Create fallback response for sprint
   */
  private AIGenerateSprintResponse createFallbackSprintResponse() {
    AIGenerateSprintResponse response = new AIGenerateSprintResponse();

    Sprint fallbackSprint = new Sprint();
    fallbackSprint.setName("AI Generation Failed");
    fallbackSprint.setGoal("Unable to generate sprint. Please try again.");
    fallbackSprint.setStatus(SprintStatus.PLANNED);
    fallbackSprint.setStartDate(LocalDate.now());
    fallbackSprint.setEndDate(LocalDate.now().plusWeeks(2));

    response.setSprint(fallbackSprint);
    response.setStories(new ArrayList<>());
    return response;
  }
}
