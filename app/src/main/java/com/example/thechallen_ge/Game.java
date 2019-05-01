package com.example.thechallen_ge;

import android.util.Log;

import java.util.ArrayList;

public class Game {
    private double grade = 0;
    public String[] days = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private int currentDay = 1;
    private int homeworkNumber = 1;
    private int quizNumber = 1;
    private int MPComplete = 0;
    private ArrayList<Lecture> lectures = new ArrayList<>();
    private int currentLectureIndex = 0;
    private boolean followed = true;
    private boolean tookQuiz = false;

    static Game shared = new Game();

    public double getGrade() {
        return grade;
    }

    public String getDayString() {
        return days[(currentDay - 1) % 7];
    }

    public int getDayInt() {
        return currentDay;
    }

    public void nextDay() {
        currentDay += 1;
        homeworkNumber += 1;
    }

    public void nextQuiz() {
        quizNumber += 1;
    }

    public int getHomeworkNumber() {
        return homeworkNumber;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public void nextLecture() {
        currentLectureIndex += 1;
    }

    public Lecture getCurrentLecture() {
        return lectures.get(currentLectureIndex);
    }

    public void addLecture(Lecture lecture) {
        lectures.add(lecture);
    }

    public boolean getFollowed() {
        return followed;
    }

    public void setFollowed(boolean f) {
        followed = f;
    }

    public boolean followGrade(long timeLeftInMilliseconds) {
        if ((timeLeftInMilliseconds >= 3000 && timeLeftInMilliseconds <= 6000) && !getFollowed()) {
            grade += 1;
            setFollowed(true);
            return true;
        }

        if ((timeLeftInMilliseconds > 6000 && timeLeftInMilliseconds <= 9000) && !getFollowed()) {
            grade += 1;
            Log.d("third slide: ", timeLeftInMilliseconds + "");
            return true;
        }

        return false;
    }

    public void incrementGrade(double num) {
        grade += num;
    }

    public boolean getTookQuiz() {
        return tookQuiz;
    }

    public void setTookQuiz(boolean q) {
        tookQuiz = q;
    }

    public int getMPComplete() {
        return MPComplete;
    }

    public void incrementMPComplete() {
        MPComplete += 1;
    }

    public void resetMPComplete() {
        MPComplete = 0;
    }

    public void reset() {
        grade = 0;
        currentDay = 1;
        homeworkNumber = 1;
        quizNumber = 1;
        MPComplete = 0;
        lectures.clear();
        currentLectureIndex = 0;
        followed = false;
        tookQuiz = false;
    }
}