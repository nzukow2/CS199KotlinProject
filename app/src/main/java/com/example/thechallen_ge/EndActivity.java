package com.example.thechallen_ge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    Button resetButton;
    TextView endTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        resetButton = findViewById(R.id.restart);
        resetButton.setOnClickListener(v -> resetButtonPressed());
        endTextView = findViewById(R.id.end_text_view);
        endTextView.setText("Chuchu says you earned a " + Game.shared.getGrade() + "% in the class.");
        Game.shared.reset();
    }

    private void resetButtonPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
