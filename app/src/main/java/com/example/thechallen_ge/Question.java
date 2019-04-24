package com.example.thechallen_ge;

public class Question {
    String category;
    String type;
    String difficulty;
    String question;
    String correctAnswer;
    String[] incorrectAnswers;

    Question(String category, String type, String difficulty, String question, String correctAnswer,
             String[] incorrectAnswers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }
}