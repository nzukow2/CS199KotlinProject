package com.example.thechallen_ge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.matthewtamlin.fortytwo.library.answer.Answer;
import com.matthewtamlin.fortytwo.library.answer.PojoAnswer;
import com.matthewtamlin.fortytwo.library.answer_group.SelectionLimitedAnswerGroup;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.util.Random;

import android.view.View;
import android.widget.TextView;
import com.matthewtamlin.fortytwo.library.answer_view.DecoratedAnswerCard;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.fortytwo.library.answer_view.ColorFadeDecorator.ColorSupplier;
import android.graphics.Color;
import android.widget.Button;


import com.matthewtamlin.fortytwo.library.answer_view.AnswerView;

public class QuizHomeworkActivity extends AppCompatActivity implements Dialog.DialogListener {

    private static final String TAG = "TheChallen_ge_QuizHomework";
    private static RequestQueue requestQueue;
    private ArrayList<Question> questions = new ArrayList<>();
    private Question displayedQuestion;
    private SelectionLimitedAnswerGroup group;
    private boolean submitButtonPressed = false;
    private Button actionButton;
    private Button OHButton;
    private boolean takingQuiz = false;
    private int questionNumber = 0;
    private int numCorrect = 0;

    private TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_homework);
        group = findViewById(R.id.answers);
        group.allowSelectionChangesWhenMarked(false);
        group.setMultipleSelectionLimit(1);
        group.enableSelectionAnimations(true);
        requestQueue = Volley.newRequestQueue(this);

        actionButton = findViewById(R.id.action_button);
        actionButton.setOnClickListener(v -> submit());

        OHButton = findViewById(R.id.officehours_button);
        OHButton.setOnClickListener(v -> goToOfficeHours());

        type = findViewById(R.id.type);

        if (questions.size() == 0) {
            startAPICall();
        } else {
            pickQuestion();
        }

        Intent intent = getIntent();
        takingQuiz = intent.getBooleanExtra("takingQuiz", false);

        TasksActivity.setUpTimer(this);
    }

    void goToOfficeHours() {
        if (submitButtonPressed) {
            return;
        }

        int index = new Random().nextInt(group.getAnswers().size());

        while (group.getAnswers().get(index).getAnswer().isCorrect()) {
            index = new Random().nextInt(group.getAnswers().size());
        }

        AnswerView toRemove = group.getAnswers().get(index);
        group.removeAnswer(toRemove);
        OHButton.setVisibility(View.INVISIBLE);
    }

    void submit() {
        if (actionButton.getText().equals("Submit")) {
            AnswerView selectedView = null;

            for (AnswerView view : group.getAnswers()) {
                if (view.isSelected()) {
                    selectedView = view;
                }
            }

            if (selectedView != null) {
                submitButtonPressed = true;

                for (AnswerView view : group.getAnswers()) {
                    view.setMarkedStatus(true, true);
                    actionButton.setText("Next Question");

                }

                if (selectedView.getAnswer().isCorrect()) {
                    numCorrect += 1;
                }

                String message = "You answered " + numCorrect + " questions correctly.";

                if ((takingQuiz && questionNumber == 3)) {

                    TasksActivity.showAlert(getSupportFragmentManager(), "Quiz Finished!", message);
                    Game.shared.incrementGrade(3.33);
                    Game.shared.nextQuiz();
                } else if (!takingQuiz) {
                    Game.shared.incrementGrade(1);
                    TasksActivity.showAlert(getSupportFragmentManager(), "Homework Finished!", message);
                }
            }

            return;
        }

        pickQuestion();
        actionButton.setText("Submit");
        submitButtonPressed = false;
    }

    void setUpUI() {
        if (questions.size() == 0) {
            return;
        }

        if (takingQuiz) {
            OHButton.setVisibility(View.INVISIBLE);
            type.setText("Quiz " + Game.shared.getQuizNumber());
        } else {
            type.setText("Homework " + Game.shared.getHomeworkNumber());
        }

        if (displayedQuestion.type.equals("boolean")) {
            OHButton.setVisibility(View.INVISIBLE);
        }

        ArrayList<Answer> answers = new ArrayList<>();

        TextView questionView = findViewById(R.id.question);
        questionView.setText(Html.fromHtml(displayedQuestion.question, Html.FROM_HTML_MODE_COMPACT));

        answers.add(new PojoAnswer(Html.fromHtml(displayedQuestion.correct_answer, Html.FROM_HTML_MODE_COMPACT), true));

        for (String incorrectAnswer: displayedQuestion.incorrect_answers) {
            answers.add(new PojoAnswer(Html.fromHtml(incorrectAnswer, Html.FROM_HTML_MODE_COMPACT), false));
        }

        for (int i = 0; i < answers.size(); i++) {
            // Like all views, a Context is needed to instantiate
            DecoratedAnswerCard answerCard = new DecoratedAnswerCard(QuizHomeworkActivity.this);

            // False = don't show animations
            answerCard.setAnswer(answers.get(i), false);

            // Identify each answer with a sequential number (e.g. 1. Some answer, 2. Another answer)
            answerCard.setIdentifier((i + 1) + ".", false);

            // Customise the answer card using decorators (see below for details)
            answerCard.addDecorator(createColorFadeDecorator(), false);

            // Show the card in the UI
            group.addAnswer(answerCard);
        }

        questionNumber += 1;
    }

    void pickQuestion() {
        group.clearAnswers();

        int index = new Random().nextInt(questions.size());
        displayedQuestion = questions.get(index);
        questions.remove(index);
        setUpUI();
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
                    response -> {
                        Log.d(TAG, response.toString());
                        parseJSON(response.toString());
                        pickQuestion();
                    }, error -> Log.w(TAG, error.toString()));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parseJSON(String json) {
        Gson g = new Gson();

        try {
            JsonParser parser = new JsonParser();
            JsonObject result = parser.parse(json).getAsJsonObject();
            JsonArray questions = result.getAsJsonArray("results");

            for (JsonElement questionElement : questions) {
                Question question = g.fromJson(questionElement, Question.class);
                this.questions.add(question);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Changes the background color of the card, using a blended color transition
    public ColorFadeDecorator createColorFadeDecorator() {
        // Defines the colors to use for different answer properties
        final ColorSupplier colorSupplier = new ColorSupplier() {
            @Override
            public int getColor(boolean marked, boolean selected, boolean answerIsCorrect) {
                if (selected && submitButtonPressed) {
                    return answerIsCorrect ? Color.GREEN : Color.RED;
                } else if (selected) {
                    return Color.DKGRAY;
                } else {
                    return Color.WHITE;
                }
            }
        };

        return new ColorFadeDecorator(colorSupplier);
    }

    @Override
    public void onOkayClicked() {
        finish();
    }
}
