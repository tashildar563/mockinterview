package com.mockinterview.mockinterview.entities;

import java.time.LocalDateTime;

public abstract class AuditableProp {
  private Long createdOn;
  private String createdBy;
  private Long updatedOn;
  private String updatedBy;
  private boolean isActive;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  // Getters and Setters
  public Long getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Long createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Long getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Long updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
}
