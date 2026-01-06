package com.mockinterview.mockinterview.entities;

import com.mockinterview.mockinterview.annotation.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Questions")
public class Question extends AuditableProp {

  @Id
  private String id;
  private String interviewId;
  @NotBlank(message = "Title is required.")
  private String title;              // Short title of the question
  private String content;
  private String difficulty;       // Optional: 1 to 5, or EASY, MEDIUM, HARD
  private String categoryCode;
  private String type;
  private String [] tags;
  private String marks;
  private String [] multipleChoices;
  private String answer;

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String[] getMultipleChoices() {
    return multipleChoices;
  }

  public void setMultipleChoices(String[] multipleChoices) {
    this.multipleChoices = multipleChoices;
  }

  public String getCategoryCode() {
    return categoryCode;
  }

  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  public String getInterviewId() {
    return interviewId;
  }

  public void setInterviewId(String interviewId) {
    this.interviewId = interviewId;
  }

  public String getMarks() {
    return marks;
  }

  public void setMarks(String marks) {
    this.marks = marks;
  }
}
