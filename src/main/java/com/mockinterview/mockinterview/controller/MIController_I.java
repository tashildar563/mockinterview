package com.mockinterview.mockinterview.controller;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.mockinterview.mockinterview.entities.Question;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "${frontend.url}")
@RestController
@RequestMapping("api/product")
public class MIController_I {



  @GetMapping("/")
  public ResponseEntity<List<Question>> getProducts(@RequestBody Map<String,Object> requestBody)
      throws Exception {
    System.out.println(response.text());
    return (ResponseEntity<List<Question>>) ResponseEntity.ok();
  }

}
