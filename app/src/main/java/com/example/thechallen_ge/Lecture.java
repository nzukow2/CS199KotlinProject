package com.example.thechallen_ge;
import java.util.ArrayList;

public class Lecture {
    private ArrayList<Integer> slides = new ArrayList<>();

    Lecture() {
    }

    public void addSlide(Integer slide) {
        slides.add(slide);
    }

    public ArrayList<Integer> getSlides() {
        return slides;
    }
}