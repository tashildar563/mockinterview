package com.mockinterview.mockinterview.entities;

import com.mockinterview.mockinterview.annotation.IsValidDate;
import com.mockinterview.mockinterview.annotation.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Interviews")
public class Interview extends AuditableProp {

  @Id
  private String id;
  @NotBlank(message = "Title is required.")
  private String title;
  private String[] candidates;
  private String mode;
  private String status;
  @IsValidDate(message = "Start date must be from tomorrow onwards")
  private long startTime;
  @IsValidDate(message = "Start date must be from tomorrow onwards")
  private long endTime;
  private Double overallScore;
  private String difficulty;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Double getOverallScore() {
    return overallScore;
  }

  public void setOverallScore(Double overallScore) {
    this.overallScore = overallScore;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String[] getCandidates() {
    return candidates;
  }

  public void setCandidates(String[] candidates) {
    this.candidates = candidates;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
}
