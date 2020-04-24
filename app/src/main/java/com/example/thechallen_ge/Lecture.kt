package com.example.thechallen_ge

import java.util.ArrayList

class Lecture internal constructor() {
    val slides = ArrayList<Int>()

    fun addSlide(slide: Int) {
        slides.add(slide)
    }
}