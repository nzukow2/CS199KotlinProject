package com.example.thechallen_ge

import android.util.Log

import java.util.ArrayList

object Game {
    var grade: Double = 0.0
        private set
    var days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var dayInt = 1
        private set
    var homeworkNumber = 1
        private set
    var quizNumber = 1
        private set
    var mpComplete = 0
        private set
    private val lectures = ArrayList<Lecture>()
    private var currentLectureIndex = 0
    var followed = true
    var tookQuiz = false

    val dayString: String
        get() = days[(dayInt - 1) % 7]

    val currentLecture: Lecture
        get() = lectures[currentLectureIndex]

    fun nextDay() {
        dayInt += 1
        homeworkNumber += 1
    }

    fun nextQuiz() {
        quizNumber += 1
    }

    fun nextLecture() {
        currentLectureIndex += 1
    }

    fun addLecture(lecture: Lecture) {
        lectures.add(lecture)
    }

    fun followGrade(timeLeftInMilliseconds: Long): Boolean {
        if (timeLeftInMilliseconds >= 3000 && timeLeftInMilliseconds <= 6000 && !followed) {
            grade += 1.0
            followed = true
            return true
        }

        if (timeLeftInMilliseconds > 6000 && timeLeftInMilliseconds <= 9000 && !followed) {
            grade += 1.0
            Log.d("third slide: ", timeLeftInMilliseconds.toString() + "")
            return true
        }

        return false
    }

    fun incrementGrade(num: Double) {
        grade += num
    }

    fun setGrade(num: Double) {
        grade = num
    }

    fun incrementMPComplete() {
        mpComplete += 1
    }

    fun resetMPComplete() {
        mpComplete = 0
    }

    fun reset() {
        grade = 0.0
        dayInt = 1
        homeworkNumber = 1
        quizNumber = 1
        mpComplete = 0
        lectures.clear()
        currentLectureIndex = 0
        followed = false
        tookQuiz = false
    }
}