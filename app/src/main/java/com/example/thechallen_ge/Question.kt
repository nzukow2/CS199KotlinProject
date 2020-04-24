package com.example.thechallen_ge

import java.util.Arrays
import android.text.Html

class Question internal constructor(internal var category: String, internal var type: String, internal var difficulty: String, internal var question: String, internal var correct_answer: String,
                                    internal var incorrect_answers: Array<String>) {

    init {

        for (i in incorrect_answers.indices) {
            this.incorrect_answers[i] = Html.fromHtml(incorrect_answers[i], Html.FROM_HTML_MODE_COMPACT).toString()
        }
    }

    override fun toString(): String {
        var toReturn = ""

        toReturn += "Category: $category"
        toReturn += "\n Type: $type"
        toReturn += "\n Difficulty: $difficulty"
        toReturn += "\n Question: $question"
        toReturn += "\n CorrectAnswer: $correct_answer"
        toReturn += "\n IncorrectAnswers: " + Arrays.toString(incorrect_answers)

        return toReturn
    }
}