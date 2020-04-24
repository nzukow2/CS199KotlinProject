package com.example.thechallen_ge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import android.widget.Button

import java.util.Timer
import java.util.TimerTask

class LectureActivity : AppCompatActivity() {
    private var geoffSlide: ViewFlipper? = null
    private var followedTextView: TextView? = null
    private var countUpTimer: Timer? = null
    private var timeLeftInMilliseconds: Long = 0
    private val timerRunning = false

    private var slideCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)

        geoffSlide = findViewById(R.id.geoffSlide)
        followedTextView = findViewById(R.id.followed_text_view)

        setUpLectures()
        for (image in Game.shared.currentLecture.slides) {
            setUpGeoffSlide(image)
        }

        countUpTimer = Timer()
        countUpTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeLeftInMilliseconds += 1000

                if (timeLeftInMilliseconds % 3 == 0L) {
                    Game.shared.followed = false
                }

                if (timeLeftInMilliseconds == 9000L) {
                    countUpTimer!!.cancel()

                    geoffSlide!!.stopFlipping()
                    Game.shared.nextLecture()
                    finish()
                }
            }
        }, 0, 1000)

        startStop()

        val followButton = findViewById<Button>(R.id.follow_button)
        followButton.setOnClickListener { v -> follow() }

        TasksActivity.setUpTimer(this)
    }

    private fun setUpGeoffSlide(image: Int) {
        val imageView = ImageView(this)
        imageView.setBackgroundResource(image)

        geoffSlide!!.addView(imageView)
        geoffSlide!!.setFlipInterval(3000)
        geoffSlide!!.isAutoStart = true

        geoffSlide!!.setInAnimation(this, android.R.anim.slide_in_left)
        geoffSlide!!.setOutAnimation(this, android.R.anim.slide_out_right)
    }

    private fun setUpLectures() {
        for (i in 1..9) {

            val lecture = Lecture()

            for (j in 1..3) {
                val slideName = "lec" + i + "_slide" + j
                val resID = resources.getIdentifier(slideName, "drawable", packageName)

                lecture.addSlide(resID)
            }

            Game.shared.addLecture(lecture)
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
        //        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
        //            @Override
        //            public void onTick(long l) {
        //                Log.d("before: ", "" + timeLeftInMilliseconds);
        //                timeLeftInMilliseconds = l;
        //                Log.d("after: ", "" + timeLeftInMilliseconds);
        //
        //                if (timeLeftInMilliseconds <= 6200 && timeLeftInMilliseconds > 5800 && Lecture.getFollowed()) {
        //                    Lecture.setFollowed(false);
        //                } else if (timeLeftInMilliseconds <= 3200 && timeLeftInMilliseconds > 2800) {
        //                    Lecture.setFollowed(false);
        //                }
        //            }
        //
        //            @Override
        //            public void onFinish() {
        //                geoffSlide.stopFlipping();
        //                stopTimer();
        //                Lecture.nextLecture();
        //
        //                Intent intent = new Intent(getApplicationContext(), TasksActivity.class);
        //                startActivity(intent);
        //                finish();
        //            }
        //        }.start();
        //        timerRunning = true;
    }

    fun stopTimer() {
        //        countDownTimer.cancel();
        //        timerRunning = false;

    }

    fun follow() {
        if (Game.shared.followGrade(timeLeftInMilliseconds)) {
            slideCounter++
            followedTextView!!.text = "Followed $slideCounter slides."
        }
    }
}
