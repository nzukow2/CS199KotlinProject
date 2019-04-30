package com.example.thechallen_ge;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class TasksActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView countDownText;
    private TextView gradeTextView;
    private TextView dayTextView;

    private CountDownTimer countDownTimer;
    private static long timeLeftInMilliseconds = 30000;
    private boolean timerRunning = false;

    Button lectureButton;
    Button homeworkButton;
    Button MPButton;
    Button quizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        lectureButton = findViewById(R.id.lecture_button);
        homeworkButton = findViewById(R.id.homework_button);
        MPButton = findViewById(R.id.MP_button);
        quizButton = findViewById(R.id.quiz_button);

        lectureButton.setOnClickListener(this);
        homeworkButton.setOnClickListener(this);
        MPButton.setOnClickListener(this);
        quizButton.setOnClickListener(this);

        countDownText = findViewById(R.id.countdown_text);
        gradeTextView = findViewById(R.id.grade_text_view);
        dayTextView = findViewById(R.id.day_text_view);

        startStop();
        setUpUI();
        setUpBasedOnDay();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpUI();
    }

    private void setUpUI() {
        gradeTextView.setText("Grade: " + Game.shared.getGrade() + "%");
        dayTextView.setText("Day: " + Game.shared.getDayInt() + " - " + Game.shared.getDayString());
    }

    private void setUpBasedOnDay() {
        lectureButton.setVisibility(View.GONE);
        homeworkButton.setVisibility(View.VISIBLE);
        MPButton.setVisibility(View.GONE);
        quizButton.setVisibility(View.GONE);

        String dayString = Game.shared.getDayString();

        if (dayString.equals("Monday") || dayString.equals("Wednesday") || dayString.equals("Friday")) {
            lectureButton.setVisibility(View.VISIBLE);
        }

        if (dayString.equals("Tuesday") || dayString.equals("Wednesday") && !Game.shared.getTookQuiz()) {
            quizButton.setVisibility(View.VISIBLE);
        }

        if (dayString.equals("Thursday")) {
            Game.shared.setTookQuiz(false);
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
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Game.shared.nextDay();
                setUpUI();
                setUpBasedOnDay();
                timeLeftInMilliseconds = 30000;

                startTimer();
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
            timeLeftText = "0:0" + seconds;
        }

        countDownText.setText(timeLeftText);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.lecture_button:
                intent = new Intent(this, LectureActivity.class);
                break;
            case R.id.homework_button:
                intent = new Intent(this, QuizHomeworkActivity.class);
                intent.putExtra("takingQuiz", false);
                break;
            case R.id.quiz_button:
                intent = new Intent(this, QuizHomeworkActivity.class);
                intent.putExtra("takingQuiz", true);
                break;
            case R.id.MP_button:
                break;
        }

        startActivity(intent);
        v.setVisibility(View.GONE);
    }

    public static CountDownTimer setUpTimer(Activity activity) {
        CountDownTimer toReturn = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
            }

            @Override
            public void onFinish() {
                activity.finish();
            }
        }.start();

        return toReturn;
    }
}