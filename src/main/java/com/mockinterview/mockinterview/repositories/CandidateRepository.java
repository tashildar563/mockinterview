package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.Actor;
import com.mockinterview.mockinterview.entities.Candidate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateRepository extends MongoRepository<Candidate, ObjectId> {

  Candidate findByemail(String email);
  Candidate findByEmailAndIsActive(String email, boolean isActive);
}
