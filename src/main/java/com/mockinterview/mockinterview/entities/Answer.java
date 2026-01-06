package com.mockinterview.mockinterview.entities;

import com.mockinterview.mockinterview.CoreConstant;
import org.springframework.data.annotation.Id;

public class Answer extends AuditableProp{
  @Id
  private String id;
  private String question_id;
  private String answer;
  private String type = CoreConstant.TEXT_U_CASE;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQuestion_id() {
    return question_id;
  }

  public void setQuestion_id(String question_id) {
    this.question_id = question_id;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
