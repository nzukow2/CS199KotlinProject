package com.example.thechallen_ge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class LectureActivity extends AppCompatActivity {
    private ViewFlipper geoffSlide;
    private TextView followedTextView;
    private Timer countUpTimer;
    private long timeLeftInMilliseconds = 0;
    private boolean timerRunning = false;

    private int slideCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        geoffSlide = findViewById(R.id.geoffSlide);
        followedTextView = findViewById(R.id.followed_text_view);

        setUpLectures();
        for (int image: Game.shared.getCurrentLecture().getSlides()) {
            setUpGeoffSlide(image);
        }

        countUpTimer = new Timer();
        countUpTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeLeftInMilliseconds += 1000;

                if (timeLeftInMilliseconds % 3 == 0) {
                    Game.shared.setFollowed(false);
                }

                if (timeLeftInMilliseconds == 9000) {
                    countUpTimer.cancel();

                    geoffSlide.stopFlipping();
                    Game.shared.nextLecture();
                    finish();
                }
            }
        }, 0, 1000);

        startStop();

        Button followButton = findViewById(R.id.follow_button);
        followButton.setOnClickListener(v -> follow());

        TasksActivity.setUpTimer(this);
    }

    private void setUpGeoffSlide(int image) {
        ImageView imageView = new ImageView( this);
        imageView.setBackgroundResource(image);

        geoffSlide.addView(imageView);
        geoffSlide.setFlipInterval(3000);
        geoffSlide.setAutoStart(true);

        geoffSlide.setInAnimation(this, android.R.anim.slide_in_left);
        geoffSlide.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private void setUpLectures() {
        for (int i = 1; i <= 9; i++) {

            Lecture lecture = new Lecture();

            for (int j = 1; j <= 3; j++) {
                String slideName = "lec" + i + "_slide" + j;
                int resID = getResources().getIdentifier(slideName, "drawable", getPackageName());

                lecture.addSlide(resID);
            }

            Game.shared.addLecture(lecture);
        }
    }

    public void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
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

    public void stopTimer() {
//        countDownTimer.cancel();
//        timerRunning = false;

    }

    public void follow() {
        if (Game.shared.followGrade(timeLeftInMilliseconds)) {
            slideCounter++;
            followedTextView.setText("Followed " + slideCounter + " slides.");
        }
    }
}
