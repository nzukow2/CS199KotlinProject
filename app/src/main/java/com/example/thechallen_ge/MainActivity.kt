package com.example.thechallen_ge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.restart).setOnClickListener({ v -> startGame() })
    }

    internal fun startGame() {
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
        finish()
    }
}