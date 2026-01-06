package com.mockinterview.mockinterview.entities;

import com.mockinterview.mockinterview.Actor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Candidates")
public class Candidate extends AuditableProp implements Actor {
  @Id
  private ObjectId id;
  private String code;
  private String name;
  private String email;
  private String mobileNumber;
  private String password;
  private String roleCode;

  @Override
  public ObjectId getId() {
    return id;
  }
  @Override
  public void setId(ObjectId id) {
    this.id = id;
  }
  @Override
  public String getCode() {
    return code;
  }
  @Override
  public void setCode(String code) {
    this.code = code;
  }
  @Override
  public String getName() {
    return name;
  }
  @Override
  public void setName(String name) {
    this.name = name;
  }
  @Override
  public String getEmail() {
    return email;
  }
  @Override
  public void setEmail(String email) {
    this.email = email;
  }
  @Override
  public String getMobileNumber() {
    return mobileNumber;
  }
  @Override
  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
  @Override
  public String getPassword() {
    return password;
  }
  @Override
  public void setPassword(String password) {
    this.password = password;
  }
  @Override
  public String getRoleCode() {
    return roleCode;
  }
  @Override
  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }
}
