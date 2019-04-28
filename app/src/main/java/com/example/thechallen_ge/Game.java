package com.example.thechallen_ge;

import android.support.v4.content.ContextCompat;

import static android.support.v4.graphics.drawable.IconCompat.getResources;
import static java.security.AccessController.getContext;

public class Game {
    int score;
    String[] days;
    Lecture[] lectures;

    Game() {
        score = 0;
        days = new String[] {"Monday", "Tuesday", "Wednesday", "Friday", "Saturday", "Sunday"};

    }

    private void setUpLectures() {
        for (int i = 1; i <= 9; i++) {

            Lecture lecture = new Lecture();

            for (int j = 1; j <= 3; j++) {
                String slideName = "lec" + i + "_slide" + j;
                Resources res = getResources();
                String
                getResources();
                ContextCompat.getDrawable(getContext(), slideName);
            }
        }
    }
}
