package com.mockinterview.mockinterview.controller;

import com.mockinterview.mockinterview.TagGenerator;
import com.mockinterview.mockinterview.entities.ApiResponse;
import com.mockinterview.mockinterview.entities.Interview;
import com.mockinterview.mockinterview.entities.Question;
import com.mockinterview.mockinterview.repositories.ActorRepositories;
import com.mockinterview.mockinterview.repositories.InterviewsRepository;
import com.mockinterview.mockinterview.request.InterviewRequest;
import com.mockinterview.mockinterview.services.EmailService;
import com.mockinterview.mockinterview.services.QuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "${frontend.url}")
@RestController
@RequestMapping("/api")
public class InterviewController {

  @Autowired
  private ActorRepositories actorRepositories;
  @Autowired
  private InterviewsRepository interviewsRepository;
  @Autowired
  private QuestionService questionService;
  @Autowired
  private EmailService emailService;

  @PostMapping("/interview")
  public ResponseEntity<ApiResponse> addQuestions(
      @RequestBody InterviewRequest interviewRequest)
      throws Exception {
    Interview interview = new Interview();
    interview.setCandidates(interview.getCandidates());
    interview.setTitle(interviewRequest.getTitle());
    interview.setActive(true);
    interview.setStartTime(interviewRequest.getStartTime());
    interview.setEndTime(interviewRequest.getEndTime());
    interview.setCreatedOn(System.currentTimeMillis());
    interview.setCreatedBy("System");
    interview.setUpdatedOn(System.currentTimeMillis());
    interview.setUpdatedBy("System");
    interview = interviewsRepository.save(interview);
    List<Question> questions = interviewRequest.getQuestions();
    for (Question question : questions) {
      question.setActive(true);
      String questionTitle = question.getTitle();
      String[] tags = TagGenerator.getInstance().extractNouns(questionTitle);
      question.setTags(tags);
      question.setCreatedBy("System");
      question.setUpdatedBy("System");
      question.setCreatedOn(System.currentTimeMillis());
      question.setUpdatedOn(System.currentTimeMillis());
      question.setInterviewId(interview.getId());
    }
    questions = questionService.saveAll(questions);
//    emailService.sendEmailToAll(interview.getCandidates());
    return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Interview Scheduled Scucessfully",interview));
  }

}
