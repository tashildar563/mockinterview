package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.entities.Question;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionsRepository extends MongoRepository<Question,Long> {

  List<Question> findAllByCreatedByAndIsActive(String id, boolean isActive);

  @Query("{ 'type': ?0, 'subject': { $regex: ?1, $options: 'i' },'isActive': true }")
  List<Question> findByTypeAndSubject(String type, String subjectRegex);
  @Query("{ 'type': ?0,'isActive': true }")
  List<Question> findByType(String type);

  @Query("{ 'type': ?0,'isActive': true }")
  List<Question> findAllByType(String type);
}
