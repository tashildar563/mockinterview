package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.entities.SessionStore;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionStoreRepository extends MongoRepository<SessionStore,String> {

  SessionStore findByEmailAndExpireAtAfter(String email, LocalDateTime now);

  SessionStore findByTokenAndValid(String token, boolean valid);

  SessionStore findByEmailAndExpireAtGreaterThanAndValid(String email, long now, boolean valid);
}
