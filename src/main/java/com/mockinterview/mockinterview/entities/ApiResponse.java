package com.mockinterview.mockinterview.entities;


public class ApiResponse<T> {
  private String status;
  private String message;
  private T data;
  private String timestamp;

  public ApiResponse(String status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.timestamp = java.time.ZonedDateTime.now().toString();
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
