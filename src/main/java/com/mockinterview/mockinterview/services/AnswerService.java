package com.mockinterview.mockinterview.services;

import java.io.*;
import java.util.*;

public class AnswerService {
    private static AnswerService answerService;

    public static AnswerService getInstance() {
        return Objects.requireNonNullElseGet(answerService, AnswerService::new);
    }

    public AnswerService() {
        try {
            File file = new File(path);
            File answersFile = new File(answersFilePath);
            File parentDirectory = file.getParentFile();
            if (parentDirectory != null && !parentDirectory.exists()) {
                boolean dirsCreated = parentDirectory.mkdirs();
                if (!dirsCreated) {
                    System.out.println("failed to create Directories : " + parentDirectory.getAbsolutePath());
                }
            }
            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    System.out.println("file created At: " + path);
                } else {
                    System.out.println("failed to create file: " + path);
                }
            } else {
                System.out.println("file Already exists at: " + path);
            }
            if (!answersFile.exists()) {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    System.out.println("file created At: " + path);
                } else {
                    System.out.println("failed to create file: " + path);
                }
            } else {
                System.out.println("file Already exists at: " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while checking / creating file: " + path);
        }
    }

    private String path = "C:\\Users\\Rakesh\\Documents\\MockInterview\\questions\\QuestionsAndAnswersHistory.csv";
    private String answersFilePath = "C:\\Users\\Rakesh\\Documents\\MockInterview\\questions\\QuestionsAndAnswers.csv";

    public boolean submit(List<String> answers) {
        return false;


    }
}
