package com.example.thechallen_ge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.matthewtamlin.fortytwo.library.answer_group.AnswerGroup;
import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup;

import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class QuizHomework extends AppCompatActivity {

    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    private ArrayList questions = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_homework);

        // Like all views, a Context is needed to instantiate
        SelectionLimitedAnswerGroup group = new SelectionLimitedAnswerGroup(QuizHomework.this);

        // Ignore user input when the answers are showing as marked
        group.allowSelectionChangesWhenMarked(false);

        // Allow at most two answers to be selected at a time
        group.setMultipleSelectionLimit(1);

        // Enable animations when the user interacts with the view
        group.enableSelectionAnimations(true);
        System.out.println(questions.size());

        if (questions.size() == 0) {
            startAPICall();
        }
    }

    /**
     * Make an API call.
     */
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://opentdb.com/api.php?amount=50&category=18",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}