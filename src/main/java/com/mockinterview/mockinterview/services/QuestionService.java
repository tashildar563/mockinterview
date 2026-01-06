package com.mockinterview.mockinterview.services;

import com.mockinterview.mockinterview.CoreConstant;
import com.mockinterview.mockinterview.TagGenerator;
import com.mockinterview.mockinterview.entities.Question;

import com.mockinterview.mockinterview.repositories.QuestionsRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class QuestionService {

  @Autowired
  JwtUtil jwtUtil;
  @Autowired
  private QuestionsRepository questionsRepository;

  public List<Question> addQuestions(List<Question> questions) throws Exception {
    TagGenerator tagGenerator = TagGenerator.getInstance();
    for (Question question : questions) {
      String[] tags = tagGenerator.extractNouns(question.getTitle());
      question.setId(null);
      question.setTags(tags);
      question.setCreatedOn(System.currentTimeMillis());
      question.setUpdatedBy(question.getCreatedBy());
      question.setUpdatedOn(System.currentTimeMillis());
      question.setActive(true);
    }
    return questionsRepository.saveAll(questions);
  }

  public List<Question> getQuestionListAtRandom(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtUtil.getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    String id = claims.get(CoreConstant.ID_L_CASE).toString();
    List<Question> addedQuestions = questionsRepository.findAllByCreatedByAndIsActive(id,true);
    return addedQuestions;
  }
  public List<Question> getQuestionList(String type, String freeText) {
    List<Question> addedQuestions =new ArrayList<>();
    if (freeText == null || freeText.trim().isEmpty()) {
      // No subject filter
      return questionsRepository.findAllByType(type);
    } else {
      // Subject search
      return questionsRepository.findByTypeAndSubject(type, freeText);
    }
  }
  public List<Question> saveAll(List<Question> questions)
  {
    return questionsRepository.saveAll(questions);
  }

  public void uploadQuestions(String id,MultipartFile file) {

      if (file == null || file.isEmpty()) {
        throw new IllegalArgumentException("CSV file is empty or missing");
      }

      List<Question> questions = new ArrayList<>();

      try (Reader reader = new BufferedReader(
          new InputStreamReader(file.getInputStream()))) {

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim()
            .parse(reader);

        for (CSVRecord record : records) {

          Question question = new Question();
          question.setTitle(record.get("title"));
          question.setContent(record.get("content"));
          question.setDifficulty(record.get("difficulty"));
          question.setCategoryCode(record.get("categoryCode"));
          question.setType(record.get("type"));
          question.setMarks(record.get("marks"));
          question.setCreatedOn(System.currentTimeMillis());
          question.setCreatedBy(id);
          question.setUpdatedBy(id);
          question.setUpdatedOn(System.currentTimeMillis());
          question.setActive(true);
          question.setAnswer(record.get("answer"));

          // Handle tags (comma-separated)
          String tags = record.get("tags");
          String multipleChoices = record.get("multipleChoices");
          question.setTags(tags != null && !tags.isEmpty()
              ? tags.split(",")
              : new String[0]);
          String [] mc = multipleChoices != null && !multipleChoices.isEmpty()
              ? multipleChoices.split("\\|")
              : new String[0];
          question.setMultipleChoices(mc);

          questions.add(question);
        }

        questionsRepository.saveAll(questions);

      } catch (Exception e) {
        throw new RuntimeException("Failed to upload CSV questions", e);
      }
    }
}
