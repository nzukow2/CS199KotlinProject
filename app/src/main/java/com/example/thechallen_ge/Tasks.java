package com.example.thechallen_ge;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class Tasks extends AppCompatActivity {
    private TextView countDownText;
    private Button countdownButton;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 30000; //30sec?
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        countDownText = findViewById(R.id.countdown_text);
        startStop();
        findViewById(R.id.takeQuiz).setOnClickListener(v -> completeTask());
    }

    public void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning = true;
    }

    public void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;

    }

    public void updateTimer() {
        int seconds = (int) timeLeftInMilliseconds / 1000;

        String timeLeftText;
        timeLeftText = "0:" + seconds;
        if (seconds < 10) {
            timeLeftText  = "0:0" + seconds;
        }

        countDownText.setText(timeLeftText);
    }

    void completeTask() {
        Intent intent = new Intent(this, QuizHomework.class);
        startActivity(intent);
        finish();
    }
}
