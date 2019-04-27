package com.example.thechallen_ge;

import java.util.Arrays;
import android.text.Html;

public class Question {
    String category;
    String type;
    String difficulty;
    String question;
    String correct_answer;
    String[] incorrect_answers;

    Question(String category, String type, String difficulty, String question, String correctAnswer,
             String[] incorrectAnswers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correctAnswer;
        this.incorrect_answers = incorrectAnswers;

        for (int i = 0; i < incorrectAnswers.length; i++) {
            this.incorrect_answers[i] = Html.fromHtml(incorrectAnswers[i], Html.FROM_HTML_MODE_COMPACT).toString();
        }
    }

    public String toString() {
        String toReturn = "";

        toReturn += "Category: " + category;
        toReturn += "\n Type: " + type;
        toReturn += "\n Difficulty: " + difficulty;
        toReturn += "\n Question: " + question;
        toReturn += "\n CorrectAnswer: " + correct_answer;
        toReturn += "\n IncorrectAnswers: " + Arrays.toString(incorrect_answers);

        return toReturn;
    }
}