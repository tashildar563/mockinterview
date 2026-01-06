package com.mockinterview.mockinterview;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockinterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockinterviewApplication.class, args);
	}
  @PostConstruct
  public void printEnv() {
    System.out.println("FRONTEND_URL env: " + System.getenv("FRONTEND_URL"));
  }

}
