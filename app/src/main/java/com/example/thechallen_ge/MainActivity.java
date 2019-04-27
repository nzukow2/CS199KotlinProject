package com.example.thechallen_ge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_course).setOnClickListener(v -> startGame());
    }

    void startGame() {
        Intent intent = new Intent(this, Tasks.class);
        startActivity(intent);
        finish();
    }

}