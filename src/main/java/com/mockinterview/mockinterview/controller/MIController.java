package com.mockinterview.mockinterview.controller;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.mockinterview.mockinterview.CoreConstant.Status;
import com.mockinterview.mockinterview.entities.ApiResponse;
import com.mockinterview.mockinterview.entities.Question;
import com.mockinterview.mockinterview.gemini.GeminiClient;
import com.mockinterview.mockinterview.services.ActorService;
import com.mockinterview.mockinterview.services.KeyService;
import com.mockinterview.mockinterview.services.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "${frontend.url}")
@RestController
@RequestMapping("api/")
public class MIController {

  @Autowired
  ActorService actorService;
  @Autowired
  KeyService keyService;
  @Autowired
  private QuestionService questionService;

  @PostMapping("/add")
  public ResponseEntity<List<Question>> addQuestions(@RequestBody List<Question> questions)
      throws Exception {
    List<Question> savedQuestions = questionService.addQuestions(questions);
    return ResponseEntity.ok(savedQuestions);
  }

  @GetMapping("/getQuestionsAtRandom")
  public ResponseEntity<ApiResponse> getQuestionsAtRandom(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse<>("FAIL", "Missing or invalid Authorization header",
              new ArrayList<>()));
    }
    String token = authHeader.substring(7);
    return ResponseEntity.ok(
        new ApiResponse<>(Status.SUCCESS_U_CASE, "Question fetched Successfully",
            questionService.getQuestionListAtRandom(token)));
  }

  @GetMapping("/questions")
  public ResponseEntity<ApiResponse> getQuestions(@RequestParam String type,
      @RequestParam(required = false) String freeText,
      @RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse<>("FAIL", "Missing or invalid Authorization header",
              new ArrayList<>()));
    }
    return ResponseEntity.ok(
        new ApiResponse<>(Status.SUCCESS_U_CASE, "Question fetched Successfully",
            questionService.getQuestionList(type, freeText)));
  }

  @PostMapping("/submit")
  public ResponseEntity<List<String>> submitAnswers(@RequestBody List<String> answers) {
//    boolean submitted = answerService.submit(answers);
    return ResponseEntity.ok(new ArrayList<>());
  }

  @PostMapping("/userCreation")
  public ResponseEntity<Map<String, Object>> createUser(
      @RequestBody Map<String, Object> userDetails) {
    userDetails.put("code", "MOCKINT-001");
    return ResponseEntity.ok(userDetails);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(@RequestBody Map<String, Object> credentials)
      throws Exception {
    return actorService.login(credentials);
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ApiResponse<>("FAIL", "Missing or invalid Authorization header", null));
    }
    String token = authHeader.substring(7); // remove "Bearer "
    return actorService.logout(token);
  }

  @PostMapping("/getActorDetails")
  public ResponseEntity<Map<String, Object>> getActorDetails(
      @RequestBody Map<String, Object> actorDetails) {
    String roleCode = actorDetails.get("roleCode").toString();
    String id = actorDetails.get("id").toString();
    Map<String, Object> credentials = actorService.getActorDetails(id, roleCode);
    credentials.put("code", "MOCKINT-002");
    return ResponseEntity.ok(credentials);
  }

  /*
   * upload csv file to bulk upload questions
   * csv template : "title","content","difficulty","categoryCode","type","tags","marks","rightAnswer"
   * */
  @PostMapping("/questions/upload-csv")
  public ResponseEntity<ApiResponse> uploadQuestions(HttpServletRequest request,
      @RequestParam("file") MultipartFile file) {
    String id = keyService.getUserId(request);
    if (file.isEmpty()) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(
              "FAIL",
              "Uploaded file is empty. Please choose a file to upload.",
              null
          ));
    }

    try {
      questionService.uploadQuestions(id, file);
      return ResponseEntity.ok(
          new ApiResponse<>("SUCCESS", "Your file is in! Upload successful.", Map.of()));
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body(new ApiResponse<>(
              "FAIL",
              e.getMessage(),
              null
          ));
    }
  }

}
