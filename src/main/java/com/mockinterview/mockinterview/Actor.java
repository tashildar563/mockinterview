package com.mockinterview.mockinterview;

import org.bson.types.ObjectId;

public interface Actor {
  ObjectId getId();
  void setId(ObjectId id);

  String getCode();
  void setCode(String code);

  String getName();
  void setName(String name);

  String getEmail();
  void setEmail(String email);

  String getMobileNumber();
  void setMobileNumber(String mobileNumber);

  String getPassword();
  void setPassword(String password);

  String getRoleCode();
  void setRoleCode(String roleCode);

}
