package com.example.thechallen_ge

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject

object NetworkManager {
    fun startAPICall(context: Context, url: String, callback: (String) -> Unit) {
        val requestQueue = Volley.newRequestQueue(context)

        try {
            val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    url, null,
                    { response ->
                        callback(response.toString())
                    }, { error -> Log.w("", error.toString()) })

            requestQueue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}