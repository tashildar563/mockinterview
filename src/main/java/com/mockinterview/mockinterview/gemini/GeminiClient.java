package com.mockinterview.mockinterview.gemini;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiClient {
  private static GeminiClient gClient;
  private final Client client;
  private GeminiClient(){
    String apiKey = System.getenv("GEMINI_API_KEY");
    if(apiKey == null || apiKey.isEmpty()){
      throw  new IllegalStateException("Gemini api key not set");
    }
    this.client = Client.builder().apiKey(apiKey).build();
  }
  public static GeminiClient getInstance() {
    if (gClient == null) {
      synchronized (GeminiClient.class) {
        if (gClient == null) {
          gClient = new GeminiClient();
        }
      }
    }
    return gClient;
  }

  public String generateText(String prompt) {
    try {
      GenerateContentResponse response =
          client.models.generateContent(
              "gemini-3-flash-preview",
              prompt,
              null);
      return response.text();
    } catch (Exception e) {
      throw new RuntimeException("Gemini generation failed", e);
    }
  }


}
