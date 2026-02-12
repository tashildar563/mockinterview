package com.mockinterview.mockinterview.SprintAI.response;

public class SprintsResponse {
  private String id;
  private String name;
  private Integer sprintNumber;

  public SprintsResponse(String id, String name, Integer sprintNumber) {
    this.id = id;
    this.name = name;
    this.sprintNumber = sprintNumber;
  }

  public String getId() { return id; }
  public String getName() { return name; }
  public Integer getSprintNumber() { return sprintNumber; }
}
