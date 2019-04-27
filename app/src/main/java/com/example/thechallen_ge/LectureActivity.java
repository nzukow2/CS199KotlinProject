package com.example.thechallen_ge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class LectureActivity extends AppCompatActivity {
    private ImageView geoffSlide;
    private ImageView yourSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        geoffSlide = findViewById(R.id.geoffSlide);
        geoffSlide.setImageResource(R.drawable.lec1_slide1);

        yourSlide = findViewById(R.id.yourSlide);
        yourSlide.setImageResource(R.drawable.lec1_slide1);
    }

    public changeGeoffSlide() {

    }


}
