package com.mockinterview.mockinterview.entities;

import com.mockinterview.mockinterview.UtilityMethods;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Document(collection = "Actor")
public class ActorEntity {
    @Id
    private Long id;
    private String code;
    private String name;
    private String email;
    private String mobileNumber;
    private LocalDateTime createdOn;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime updatedOn;
    private String password;

    public ActorEntity(){
    }

    public ActorEntity(Map<String, Object> actorDetails) {
        this.id = System.currentTimeMillis();
        this.name = UtilityMethods.stringOf(actorDetails.get("name"));
        this.email = UtilityMethods.stringOf(actorDetails.get("email"));
        this.mobileNumber= UtilityMethods.stringOf(actorDetails.get("mobile_number"));
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
        this.createdBy = "System";
        this.updatedBy = "System";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
