package com.example.thechallen_ge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import kotlin.math.floor
import kotlin.math.*

class ExtraCredit : AppCompatActivity(), TasksActivityDelegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_credit)
        var x = findViewById<ImageView>(R.id.ecButton)
        x.setOnClickListener() {
            bean = true
            Log.i("test", "This is a clicker button test")
            Game.incrementGrade(0.15)
            Log.i("test", "Grade is: " + Game.grade)
            Log.i("test","bean is currently: " + bean)
        }
        TasksActivity.setUpTimer(this)
    }

    var bean = false

    override fun timeLeft(timeLeftInMilliseconds: Long) {
        //TODO("Not yet implemented")
        Log.i("test", "Outside the timeleft if!")
        Log.i("test","The time left is: " + timeLeftInMilliseconds)
        if (bean && timeLeftInMilliseconds <= 5000) {
            var x = Game.grade
            x = Math.floor(x)
            Game.setGrade(x)
            bean = false
        }
    }
}