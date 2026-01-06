package com.mockinterview.mockinterview.entities;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "session_store")
public class SessionStore {
  @Id
  private String id;
  private String email;
  private String token;
  private Long issuedAt;
  private Long expireAt;
  private boolean valid = true;
  private long createdOn;
  private String createdBy;
  private long updatedOn;
  private String updatedBy;
  private boolean isActive;

  public SessionStore(String email, String token, Long issued_at,
      Long expires_at) {
    this.email = email;
    this.token = token;
    this.issuedAt = issued_at;
    this.expireAt = expires_at;
  }

  public SessionStore() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public boolean getValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(Long issuedAt) {
    this.issuedAt = issuedAt;
  }

  public Long getExpiresAt() {
    return expireAt;
  }

  public void setExpiresAt(Long expiresAt) {
    this.expireAt = expiresAt;
  }

  public boolean isValid() {
    return valid;
  }

  public Long getExpireAt() {
    return expireAt;
  }

  public void setExpireAt(Long expireAt) {
    this.expireAt = expireAt;
  }

  public long getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(long createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public long getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(long updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }
}
