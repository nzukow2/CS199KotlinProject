package com.example.thechallen_ge

import android.content.ClipData.Item
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.thechallen_ge.TasksActivity.Companion.showAlert
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity(), Dialog.DialogListener {

    var muteMusic = false
    var staff: List<Staff>? = null
    var music : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainMenuInitializer()
        startMainMusic()
    }

    fun startMainMusic() {
        music = MediaPlayer.create(this, R.raw.music)
        music?.start()
        music?.setLooping(true)
        music?.setVolume(10000F, 10000F)
    }

    //Initializes all the buttons in the main menu
    fun mainMenuInitializer() {
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.restart).setOnClickListener({ v -> startGame() })

        //Takes us to the cheat terminal
        val gotoTerminal = findViewById<View>(R.id.gotoTerminalButton)
        gotoTerminal.setOnClickListener {
            setContentView(R.layout.cheat_terminal)
            cheatTerminal()
        }

        //Takes us to settings
        val gotoSettings = findViewById<View>(R.id.gotoSettingsMenuButton)
        gotoSettings.setOnClickListener {
            setContentView(R.layout.setting_menu)
            settingsMenu()
        }
    }

    fun settingsMenu() {
        val x = findViewById<Button>(R.id.muteMusicButton)
        x.setOnClickListener {
            muteMusic = !muteMusic // Note
            Log.i("test", "music is muted: " + muteMusic)
        }
        val backToMain = findViewById<Button>(R.id.returnToMainFromSettings)
        backToMain.setOnClickListener {
            mainMenuInitializer()
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

            //staff = staffJsonArray.map { g.fromJson(it, Staff::class.java) }

//            staff = staffJsonArray.map {
//                val staffJsonObject = it.asJsonObject
//                Staff(staffJsonObject.get("name").asString, staffJsonObject.get("email").asString, Role.HEAD)
//            }

            staff = Gson().fromJson(staffJsonArray,
                    object : TypeToken<List<Staff>>(){}.type)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //Starts the game
    private fun startGame() {
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Cheat Terminal
    fun cheatTerminal() {
        startAPICall()

        findViewById<Button>(R.id.leaveTerminal).setOnClickListener {
            mainMenuInitializer()
        }

        findViewById<Button>(R.id.unlockButton).setOnClickListener {
            val input = findViewById<EditText>(R.id.terminalEditText).text.toString()
            val staffMember = staff?.firstOrNull { it.name == input } ?: return@setOnClickListener

            val extraCredit = when (staffMember.role) {
                Role.HEAD -> 35.00
                Role.TA -> 30.00
                Role.DEVELOPER -> 25.00
                Role.CAPTAIN -> 20.00
                Role.ASSOCIATE -> 15.00
                Role.ASSISTANT -> 10.00
            }

            Game.incrementGrade(extraCredit)

            val title = "Extra Credit"
            val message = "You earned $extraCredit% extra credit!"

            showAlert(supportFragmentManager, title, message)
        }
    }

    override fun onOkayClicked() { }
}