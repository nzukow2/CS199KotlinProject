package com.example.thechallen_ge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Tasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        findViewById(R.id.takeQuiz).setOnClickListener(v -> completeTask());
    }

    void completeTask() {
        Intent intent = new Intent(this, QuizHomework.class);
        startActivity(intent);
        finish();
    }
}
