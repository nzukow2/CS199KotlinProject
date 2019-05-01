package com.example.thechallen_ge;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.view.WindowManager;

public class TasksActivity extends AppCompatActivity implements View.OnClickListener, Dialog.DialogListener {
    private TextView countDownText;
    private TextView gradeTextView;
    private TextView dayTextView;
    private TextView MPTextView;

    private CountDownTimer countDownTimer;
    private static long timeLeftInMilliseconds;
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
        MPTextView = findViewById(R.id.mp_text_view);

        timeLeftInMilliseconds = 30000;

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
        MPButton.setText("Work on MP - " + Game.shared.getMPComplete() +"/3 Complete");
    }

    private void setUpBasedOnDay() {
        lectureButton.setVisibility(View.GONE);
        homeworkButton.setVisibility(View.VISIBLE);
        quizButton.setVisibility(View.GONE);

        String dayString = Game.shared.getDayString();

        if (Game.shared.getDayInt() == 11) {
            MPButton.setVisibility(View.VISIBLE);
            MPButton.setText("Work on MP - " + Game.shared.getMPComplete() +"/3 Complete");
            Game.shared.resetMPComplete();
            MPTextView.setVisibility(View.INVISIBLE);
        }

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
                checkIfEnd();
                Game.shared.nextDay();
                setUpUI();
                setUpBasedOnDay();
                timeLeftInMilliseconds = 30000;
                startTimer();
            }
        }.start();
        timerRunning = true;
    }

    public void checkIfEnd() {
        if (Game.shared.getDayInt() == 21) {
            Intent intent = new Intent(this, EndActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void MPTimer() {
        new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                MPTextView.setVisibility(View.INVISIBLE);

                Game.shared.incrementGrade(5);
                Game.shared.incrementMPComplete();

                setUpUI();

                if (Game.shared.getMPComplete() == 3) {
                    MPButton.setVisibility(View.INVISIBLE);
                    Game.shared.resetMPComplete();
                }
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
                Game.shared.setTookQuiz(true);
                break;
            case R.id.MP_button:
                if (timeLeftInMilliseconds < 20000) {
                    String title = "Not Enough Time";
                    String message = "You do not have enough time today to work on the MP.";

                    TasksActivity.showAlert(getSupportFragmentManager(), title, message);

                    return;
                }

                MPTextView.setVisibility(View.VISIBLE);
                MPTimer();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        if (intent != null) {
            startActivity(intent);
            v.setVisibility(View.GONE);
        }
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

    public static void showAlert(FragmentManager manager, String title, String message) {
        Dialog dialog = new Dialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        dialog.setArguments(args);

        dialog.show(manager, "");
    }

    @Override
    public void onOkayClicked() {}
}