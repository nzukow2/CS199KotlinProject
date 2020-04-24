package com.example.thechallen_ge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainMenuInitializer()
    }

    //Initializes all the buttons in the main menu
    fun mainMenuInitializer() {
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.restart).setOnClickListener({ v -> startGame() })

        //Takes us to the cheat terminal
        val x = findViewById<View>(R.id.gotoTerminalButton)
        x.setOnClickListener {
            setContentView(R.layout.cheat_terminal)
            cheatTerminal()
        }
    }

    //Starts the game
    internal fun startGame() {
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
        finish()
    }

    //
    fun cheatTerminal() {
        val y = findViewById<View>(R.id.leaveTerminal)
        y.setOnClickListener {
            setContentView(R.layout.activity_main)
            mainMenuInitializer()
        }
    }
}