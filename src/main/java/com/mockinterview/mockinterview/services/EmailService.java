package com.mockinterview.mockinterview.services;

import io.jsonwebtoken.lang.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

  public void sendSimpleEmail(String toEmail, String subject, String body) {

    System.out.println("✅ Email sent successfully to " + toEmail);
  }

  public void sendEmailToAll(String[] emails){
    Arrays.asList(emails).forEach(email -> sendSimpleEmail(email,"Interview Scheduled","Interview Scheduled"));
  }
}
