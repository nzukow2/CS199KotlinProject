package com.example.thechallen_ge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.matthewtamlin.fortytwo.library.answer.Answer
import com.matthewtamlin.fortytwo.library.answer.PojoAnswer
import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup
import java.util.ArrayList
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonObject
import java.util.Random

import android.view.View
import android.widget.TextView
import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator.ColorSupplier
import android.graphics.Color
import android.widget.Button


import com.matthewtamlin.fortytwo.library.answer_view.AnswerView
import org.json.JSONObject

class QuizHomeworkActivity : AppCompatActivity(), Dialog.DialogListener {
    private val questions = ArrayList<Question>()
    private var displayedQuestion: Question? = null
    private var group: SelectionLimitedAnswerGroup? = null
    private var submitButtonPressed = false
    private var actionButton: Button? = null
    private var OHButton: Button? = null
    private var takingQuiz = false
    private var questionNumber = 0
    private var numCorrect = 0

    private var type: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz_homework)
        group = findViewById(R.id.answers)
        group!!.allowSelectionChangesWhenMarked(false)
        group!!.multipleSelectionLimit = 1
        group!!.enableSelectionAnimations(true)
        requestQueue = Volley.newRequestQueue(this)

        actionButton = findViewById(R.id.action_button)
        actionButton!!.setOnClickListener { v -> submit() }

        OHButton = findViewById(R.id.officehours_button)
        OHButton!!.setOnClickListener { v -> goToOfficeHours() }

        type = findViewById(R.id.type)

        if (questions.size == 0) {
            startAPICall()
        } else {
            pickQuestion()
        }

        val intent = intent
        takingQuiz = intent.getBooleanExtra("takingQuiz", false)

        TasksActivity.setUpTimer(this)
    }

    internal fun goToOfficeHours() {
        if (submitButtonPressed) {
            return
        }

        var index = Random().nextInt(group!!.answers.size)

        while (group!!.answers[index].answer.isCorrect) {
            index = Random().nextInt(group!!.answers.size)
        }

        val toRemove = group!!.answers[index]
        group!!.removeAnswer(toRemove)
        OHButton!!.visibility = View.INVISIBLE
    }

    internal fun submit() {
        if (actionButton!!.text == "Submit") {
            var selectedView: AnswerView? =
                    null

            for (view in group!!.answers) {
                if (view.isSelected) {
                    selectedView = view
                }
            }

            if (selectedView != null) {
                submitButtonPressed = true

                for (view in group!!.answers) {
                    view.setMarkedStatus(true, true)
                    actionButton!!.text = "Next Question"

                }

                if (selectedView.answer.isCorrect) {
                    numCorrect += 1
                }

                val message = "You answered $numCorrect questions correctly."

                if (takingQuiz && questionNumber == 3) {

                    TasksActivity.showAlert(supportFragmentManager, "Quiz Finished!", message)
                    Game.incrementGrade(3.33)
                    Game.nextQuiz()
                } else if (!takingQuiz) {
                    Game.incrementGrade(1.0)
                    TasksActivity.showAlert(supportFragmentManager, "Homework Finished!", message)
                }
            }

            return
        }

        pickQuestion()
        actionButton!!.text = "Submit"
        submitButtonPressed = false
    }

    internal fun setUpUI() {
        if (questions.size == 0) {
            return
        }

        if (takingQuiz) {
            OHButton!!.visibility = View.INVISIBLE
            type!!.text = "Quiz " + Game.quizNumber
        } else {
            type!!.text = "Homework " + Game.homeworkNumber
        }

        if (displayedQuestion!!.type.equals("boolean")) {
            OHButton!!.visibility = View.INVISIBLE
        }

        val answers = ArrayList<Answer>()

        val questionView = findViewById<TextView>(R.id.question)
        questionView.text = Html.fromHtml(displayedQuestion!!.question, Html.FROM_HTML_MODE_COMPACT)

        answers.add(PojoAnswer(Html.fromHtml(displayedQuestion!!.correct_answer, Html.FROM_HTML_MODE_COMPACT), true))

        for (incorrectAnswer in displayedQuestion!!.incorrect_answers) {
            answers.add(PojoAnswer(Html.fromHtml(incorrectAnswer, Html.FROM_HTML_MODE_COMPACT), false))
        }

        for (i in answers.indices) {
            // Like all views, a Context is needed to instantiate
            val answerCard = DecoratedAnswerCard(this@QuizHomeworkActivity)

            // False = don't show animations
            answerCard.setAnswer(answers[i], false)

            // Identify each answer with a sequential number (e.g. 1. Some answer, 2. Another answer)
            answerCard.setIdentifier((i + 1).toString() + ".", false)

            // Customise the answer card using decorators (see below for details)
            answerCard.addDecorator(createColorFadeDecorator(), false)

            // Show the card in the UI
            group!!.addAnswer(answerCard)
        }

        questionNumber += 1
    }

    internal fun pickQuestion() {
        group!!.clearAnswers()

        val index = Random().nextInt(questions.size)
        displayedQuestion = questions[index]
        questions.removeAt(index)
        setUpUI()
    }

    /**
     * Make an API call.
     */
    private fun startAPICall() {
        val url = "https://opentdb.com/api.php?amount=50&category=18"

        NetworkManager.startAPICall(this, url) { response: String ->
            parseJSON(response)
            pickQuestion()
        }
    }

    private fun parseJSON(json: String) {
        val g = Gson()

        try {
            val parser = JsonParser()
            val result = parser.parse(json).asJsonObject
            val questions = result.getAsJsonArray("results")

            for (questionElement in questions) {
                val question = g.fromJson(questionElement, Question::class.java)
                this.questions.add(question)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // Changes the background color of the card, using a blended color transition
    fun createColorFadeDecorator(): ColorFadeDecorator {
        // Defines the colors to use for different answer properties
        val colorSupplier = ColorSupplier { marked, selected, answerIsCorrect ->
            if (selected && submitButtonPressed) {
                if (answerIsCorrect) Color.GREEN else Color.RED
            } else if (selected) {
                Color.DKGRAY
            } else {
                Color.WHITE
            }
        }

        return ColorFadeDecorator(colorSupplier)
    }

    override fun onOkayClicked() {
        finish()
    }

    companion object {

        private val TAG = "TheChallen_ge_QuizHomework"
        private var requestQueue: RequestQueue? = null
    }
}
