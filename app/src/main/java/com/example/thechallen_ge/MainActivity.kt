package com.example.thechallen_ge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonParser

class MainActivity : AppCompatActivity() {

    var staff: List<Staff>? = null

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

    /**
     * Make an API call.
     */
    private fun startAPICall() {
        val url = "https://raw.githubusercontent.com/cs125-illinois/www/master/src/info/course.json"

        NetworkManager.startAPICall(this, url) { response: String ->
            parseJSON(response)
        }
    }

    private fun parseJSON(json: String) {
        val g = Gson()

        try {
            val parser = JsonParser()
            val result = parser.parse(json).asJsonObject
            val staffJsonArray = result.getAsJsonArray("staff")

            staff = staffJsonArray.map { g.fromJson(it, Staff::class.java) }

        } catch (e: Exception) {
            e.printStackTrace()
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
        startAPICall()

        val y = findViewById<View>(R.id.leaveTerminal)
        y.setOnClickListener {
            setContentView(R.layout.activity_main)
            mainMenuInitializer()
        }

        val ben = staff?.firstOrNull { it.name == "Ben Nordick" } ?: return
    }
}