package com.example.thechallen_ge

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONObject

class NetworkManager {
    internal fun parseJSON(json: String) {
        val g = Gson()

        try {
            val parser = JsonParser()
            val result = parser.parse(json).asJsonObject
            val questions = result.getAsJsonArray("results")

            for (questionElement in questions) {
                val question = g.fromJson(questionElement, Question::class.java)
                //this.questions.add(question)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private var requestQueue: RequestQueue? = null


        companion object Internal fun startAPICall(context: Context, callback: (JSONObject) -> Unit) {
        requestQueue = Volley.newRequestQueue(this)
        try {
            val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    "https://opentdb.com/api.php?amount=50&category=18", null,
                    { response ->
                        Log.d(QuizHomeworkActivity.TAG, response.toString())
                        callback(response)
                    }, { error -> Log.w(QuizHomeworkActivity.TAG, error.toString()) })


            QuizHomeworkActivity.requestQueue.add<JSONObject>(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}