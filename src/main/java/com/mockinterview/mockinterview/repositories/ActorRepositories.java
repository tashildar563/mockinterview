package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.entities.ActorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepositories extends MongoRepository<ActorEntity,Long> {
    ActorEntity findByemail(Object email);
}
