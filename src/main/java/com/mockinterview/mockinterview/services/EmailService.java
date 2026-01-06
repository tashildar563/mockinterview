package com.mockinterview.mockinterview.services;

import io.jsonwebtoken.lang.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  public void sendSimpleEmail(String toEmail, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("tashildar563@gmail.com");
    message.setTo(toEmail);
    message.setSubject(subject);
    message.setText(body);

    mailSender.send(message);
    System.out.println("✅ Email sent successfully to " + toEmail);
  }

  public void sendEmailToAll(String[] emails){
    Arrays.asList(emails).forEach(email -> sendSimpleEmail(email,"Interview Scheduled","Interview Scheduled"));
  }
}
