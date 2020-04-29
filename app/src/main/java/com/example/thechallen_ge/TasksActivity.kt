package com.example.thechallen_ge

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Button
import android.view.WindowManager
import com.example.thechallen_ge.TasksActivity.Companion.timeLeftInMilliseconds

class TasksActivity : AppCompatActivity(), View.OnClickListener, Dialog.DialogListener {
    private var countDownText: TextView? = null
    private var gradeTextView: TextView? = null
    private var dayTextView: TextView? = null
    private var MPTextView: TextView? = null

    private var countDownTimer: CountDownTimer? = null
    private var timerRunning = false

    lateinit var lectureButton: Button
    lateinit var  homeworkButton: Button
    lateinit var MPButton: Button
    lateinit var quizButton: Button
    lateinit var extraCreditButton: Button

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        lectureButton = findViewById(R.id.lecture_button)
        homeworkButton = findViewById(R.id.homework_button)
        MPButton = findViewById(R.id.MP_button)
        quizButton = findViewById(R.id.quiz_button)
        extraCreditButton = findViewById(R.id.gotoExtraCredit)

        lectureButton.setOnClickListener(this)
        homeworkButton.setOnClickListener(this)
        MPButton.setOnClickListener(this)
        quizButton.setOnClickListener(this)
        extraCreditButton.setOnClickListener(this)

        countDownText = findViewById(R.id.countdown_text)
        gradeTextView = findViewById(R.id.grade_text_view)
        dayTextView = findViewById(R.id.day_text_view)
        MPTextView = findViewById(R.id.mp_text_view)

        timeLeftInMilliseconds = 30000

        startStop()
        setUpUI()
        setUpBasedOnDay()
    }

    override fun onStart() {
        super.onStart()

        setUpUI()
    }

    private fun setUpUI() {
        gradeTextView!!.text = "Grade: " + Game.grade + "%"
        dayTextView!!.text = "Day: " + Game.dayInt + " - " + Game.dayString
        MPButton.text = "Work on MP - " + Game.mpComplete + "/3 Complete"
    }

    private fun setUpBasedOnDay() {
        lectureButton.visibility = View.GONE
        homeworkButton.visibility = View.VISIBLE
        quizButton.visibility = View.GONE

        val dayString = Game.dayString

        if (Game.dayInt === 11) {
            MPButton.visibility = View.VISIBLE
            MPButton.text = "Work on MP - " + Game.mpComplete + "/3 Complete"
            Game.resetMPComplete()
            MPTextView!!.visibility = View.INVISIBLE
        }

        if (dayString == "Monday" || dayString == "Wednesday" || dayString == "Friday") {
            lectureButton.visibility = View.VISIBLE
        }

        if (dayString == "Tuesday" || dayString == "Wednesday" && !Game.tookQuiz) {
            quizButton.visibility = View.VISIBLE
        }

        if (dayString == "Thursday") {
            Game.tookQuiz
        }
    }

    fun startStop() {
        if (timerRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMilliseconds, 1000) {
            override fun onTick(l: Long) {
                timeLeftInMilliseconds = l
                updateTimer()
            }

            override fun onFinish() {
                checkIfEnd()
                Game.nextDay()
                setUpUI()
                setUpBasedOnDay()
                timeLeftInMilliseconds = 30000
                startTimer()
            }
        }.start()
        timerRunning = true
    }

    fun checkIfEnd() {
        if (Game.dayInt === 21) {
            val intent = Intent(this, EndActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun MPTimer() {
        object : CountDownTimer(20000, 1000) {
            override fun onTick(l: Long) {}

            override fun onFinish() {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                MPTextView!!.visibility = View.INVISIBLE

                Game.incrementGrade(5.0)
                Game.incrementMPComplete()

                setUpUI()

                if (Game.mpComplete === 3) {
                    MPButton.visibility = View.INVISIBLE
                    Game.resetMPComplete()
                }
            }
        }.start()
        timerRunning = true
    }

    fun stopTimer() {
        countDownTimer!!.cancel()
        timerRunning = false

    }

    fun updateTimer() {
        val seconds = timeLeftInMilliseconds.toInt() / 1000

        var timeLeftText: String
        timeLeftText = "0:$seconds"
        if (seconds < 10) {
            timeLeftText = "0:0$seconds"
        }

        countDownText!!.text = timeLeftText
    }

    override fun onClick(v: View) {
        var intent: Intent? = null

        when (v.id) {
            R.id.lecture_button -> intent = Intent(this, LectureActivity::class.java)
            R.id.homework_button -> {
                intent = Intent(this, QuizHomeworkActivity::class.java)
                intent.putExtra("takingQuiz", false)
            }
            R.id.quiz_button -> {
                intent = Intent(this, QuizHomeworkActivity::class.java)
                intent.putExtra("takingQuiz", true)
                Game.tookQuiz
            }
            R.id.MP_button -> {
                if (timeLeftInMilliseconds < 20000) {
                    val title = "Not Enough Time"
                    val message = "You do not have enough time today to work on the MP."

                    showAlert(supportFragmentManager, title, message)

                    return
                }

                MPTextView!!.visibility = View.VISIBLE
                MPTimer()
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            R.id.gotoExtraCredit -> {
                intent = Intent(this, ExtraCredit::class.java)
            }
        }

        if (intent != null) {
            startActivity(intent)
            v.visibility = View.GONE
        }
    }

    override fun onOkayClicked() {}

    companion object {
        private var timeLeftInMilliseconds: Long = 0

        fun setUpTimer(activity: Activity): CountDownTimer {

            return object : CountDownTimer(timeLeftInMilliseconds, 1000) {
                override fun onTick(l: Long) {
                    timeLeftInMilliseconds = l
                }

                override fun onFinish() {
                    activity.finish()
                }
            }.start()
        }

        fun showAlert(manager: FragmentManager, title: String, message: String) {
            val dialog = Dialog()

            val args = Bundle()
            args.putString("title", title)
            args.putString("message", message)
            dialog.arguments = args

            dialog.show(manager, "")
        }
    }
}