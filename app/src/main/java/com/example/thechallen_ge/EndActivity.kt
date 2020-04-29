package com.example.thechallen_ge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EndActivity : AppCompatActivity() {

    private lateinit var resetButton: Button
    private lateinit var endTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        resetButton = findViewById(R.id.restart)
        resetButton.setOnClickListener { v -> resetButtonPressed() }
        endTextView = findViewById(R.id.end_text_view)
        endTextView.text = "Chuchu says you earned a " + Game.grade + "% in the class."
        Game.reset()
    }

    private fun resetButtonPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
