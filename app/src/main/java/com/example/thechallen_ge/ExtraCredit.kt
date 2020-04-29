package com.example.thechallen_ge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView

class ExtraCredit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_credit)
        var x = findViewById<ImageView>(R.id.ecButton)
        x.setOnClickListener() {
            Log.i("test","This is a clicker button test")
        }
    }

}