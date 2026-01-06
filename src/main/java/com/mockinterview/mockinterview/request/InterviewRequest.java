package com.mockinterview.mockinterview.request;

import com.mockinterview.mockinterview.annotation.IsValidDate;
import com.mockinterview.mockinterview.annotation.NotBlank;
import com.mockinterview.mockinterview.entities.Question;
import java.util.List;

public class InterviewRequest {
  @NotBlank(message = "Title is required.")
  private String title;
  private boolean isPartOfInterview;
  private String[] candidates;
  @IsValidDate(message = "Start date must be from tomorrow onwards")
  private long startTime;
  @IsValidDate(message = "Start date must be from tomorrow onwards")
  private long endTime;
  private List<Question> questions;
  private String interviewer;
  private String location;
  private String remarks;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isPartOfInterview() {
    return isPartOfInterview;
  }

  public void setPartOfInterview(boolean partOfInterview) {
    isPartOfInterview = partOfInterview;
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

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public String getInterviewer() {
    return interviewer;
  }

  public void setInterviewer(String interviewer) {
    this.interviewer = interviewer;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}
