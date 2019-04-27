package com.example.thechallen_ge;

import android.media.Image;
import java.util.List;
import java.util.ArrayList;

public class Lecture {
    private ArrayList<Image> slides = new ArrayList<>();

    Lecture() {
    }

    public void addSlide(Image slide) {
        slides.add(slide);
    }
}
