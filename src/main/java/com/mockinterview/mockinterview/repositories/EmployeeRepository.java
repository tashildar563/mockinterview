package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.Actor;
import com.mockinterview.mockinterview.entities.Employee;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, ObjectId> {

  Actor findByemail(String username);
}
