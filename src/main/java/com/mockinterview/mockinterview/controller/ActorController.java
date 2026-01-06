package com.mockinterview.mockinterview.controller;

import com.mockinterview.mockinterview.Actor;
import com.mockinterview.mockinterview.UtilityMethods;
import com.mockinterview.mockinterview.entities.ApiResponse;
import com.mockinterview.mockinterview.entities.Candidate;
import com.mockinterview.mockinterview.entities.Employee;
import com.mockinterview.mockinterview.repositories.CandidateRepository;
import com.mockinterview.mockinterview.repositories.EmployeeRepository;
import com.mockinterview.mockinterview.services.ActorService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
  @Autowired private CandidateRepository candidateRepository;
  @Autowired private ActorService actorFactory;

  @PostMapping("/createAnActor")
  public ResponseEntity<?> createActor(@RequestBody Map<String,Object> request) {
    Actor actor = actorFactory.createActor(request);
    if(actor==null){
      return ResponseEntity.badRequest().body(new ApiResponse<>("FAIL","This email is already associated with an existing user account.",request));
    }
    if (actor instanceof Employee) {
      Employee saved = new Employee();
      return ResponseEntity.ok(saved);
    } else if (actor instanceof Candidate) {
      Candidate saved = candidateRepository.save((Candidate) actor);
      return ResponseEntity.ok(new ApiResponse<>("SUCCESS","User registration completed successfully.",request));
    } else {
      return ResponseEntity.badRequest().body(new ApiResponse<>("FAIL","Invalid Role",request));
    }
  }
  @PostMapping("/deleteAnActor")
  public ResponseEntity<?> deleteAnActor(@RequestBody Map<String,Object> request) {
    String role = request.get("roleCode").toString();
    String id = request.get("id").toString();
    Actor actor = (Actor) actorFactory.deleteAnActor(id,role);
    if (actor instanceof Employee) {
      Employee saved = new Employee();
      return ResponseEntity.ok(saved);
    } else if (actor instanceof Candidate) {
      Candidate saved = candidateRepository.save((Candidate) actor);
      return ResponseEntity.ok(saved);
    } else {
      return ResponseEntity.badRequest().body("Unsupported actor type");
    }
  }
  @PostMapping("/resetPassword")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String,Object> request) {
    String email = UtilityMethods.stringOf(request.get("email"));
    String password = UtilityMethods.stringOf(request.get("password"));
    Actor actor = actorFactory.resetPassword(email, password);
    if(actor==null){
      return ResponseEntity.badRequest().body(new ApiResponse<>("FAIL","We couldn't update your password. Please check your input and try again.",request));
    }
    if (actor instanceof Employee) {
      Employee saved = new Employee();
      return ResponseEntity.ok(saved);
    } else if (actor instanceof Candidate) {
      Candidate saved = candidateRepository.save((Candidate) actor);
      return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Your password has been updated. You can now log in with the new credentials.",saved));
    } else {
      return ResponseEntity.badRequest().body(new ApiResponse<>("FAIL","Invalid Role",request));
    }
  }

}
