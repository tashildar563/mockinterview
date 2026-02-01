package com.mockinterview.mockinterview.repositories;

import com.mockinterview.mockinterview.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,Integer> {

}
