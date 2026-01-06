package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.entities.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewsRepository extends MongoRepository<Interview,Long> {
}
