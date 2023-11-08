package com.example.quizzicalpursuit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {
    /**------Question variables------**/
    int currentQuestionIndex = 0; //The index of the question we're on currently
    List<Question> triviaQuestions; //The list of questions

    /**------Our settings------**/
    int numOfQuestions; //The number of questions in the game
    int timeLimitPerQuestion; //The time in seconds that is given per question
    boolean backgroundMusic; //does the user want backgroundMusic
    boolean sfx; //Does the user want sound fx
    int categoryID;
    String categoryName;

    /**------Metric Gathering Variables------**/
    long startTime, endTime; //Used to calculate our time spent
    ArrayList<Long> questionTimes; //A log of the amount of time spent per question -- timeSpent = endTime - startTime;
    int correctAnswerCount = 0; //The number of correct answers

    /**------Android Control Variables------**/
    ExecutorService executorService;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnQuit;
    TextView tvQuestion, tvProgress;
    ProgressBar pbarCountdown;
    SharedPreferences triviaSettings;
    Intent intent;
    CountDownTimer timer;
    public static final String TAG = "SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Load our intent, settings, and get our question times ready
        triviaSettings = getSharedPreferences(TAG, Context.MODE_PRIVATE);
        questionTimes = new ArrayList<>();
        intent = getIntent();

        //Now that those are defined, lets populate those.
        populateSettings();

        //Set up our thread for grabbing our questions
        executorService = Executors.newSingleThreadExecutor(); // Initialize ExecutorService

        //Find all our controls
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        btnQuit = findViewById(R.id.btnQuit);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvProgress = findViewById(R.id.tvProgress);
        pbarCountdown = findViewById(R.id.pBarCountdown);

        resetBtnColor();

        // Create a list of questions by calling fetchQuestions on a background thread using ExecutorService.submit
        executorService.submit(() -> {

            List<Question> triviaQuestions = fetchQuestions();
            Log.d("question", triviaQuestions.toString());

            if (triviaQuestions != null && !triviaQuestions.isEmpty())
            {
                // Populate buttons with questions and answers
                populateButtonsWithQuestions(triviaQuestions, 0);
            }
            else
            {
                // Handle the case where no questions are available
                Toast.makeText(this, "Unable to get questions. Please try again.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        });

        //If the user clicks this, they want to leave, so we go back.
        btnQuit.setOnClickListener((view)->{
            timer.cancel();
            this.finish();
            GameSounds.endSound(this);
        });
    }

    private void populateSettings()
    {
        //Grab our settings from the sharedPrefs
        numOfQuestions = triviaSettings.getInt("numQuestions", 10);
        timeLimitPerQuestion = triviaSettings.getInt("timePerQuestion", 30);

        //TODO - do we need this bit of code? I think NOT. Welll...maybe
        String sounds = triviaSettings.getString("sounds", "music, sfx");
        backgroundMusic = sounds.toLowerCase().contains("music");
        sfx = sounds.toLowerCase().contains("sfx");

        //Grab our category from the intent
        Bundle bundle = intent.getExtras();
        categoryName = bundle.getString("CAT");
        categoryID = bundle.getInt("CATID");

        Log.d("Category", "" + categoryID);
        Log.d("Category", "" + categoryName);
    }

    private void populateButtonsWithQuestions(List<Question> questions, int questionNum)
    {
        if (questions != null && !questions.isEmpty())
        {

            Question question = questions.get(questionNum);
            String questionText = question.getQuestion();
            String correctAnswer = question.getCorrectAnswer();
            List<String> incorrectAnswers = new ArrayList<>();
            incorrectAnswers.add(question.getIncorrectAnswer1());
            incorrectAnswers.add(question.getIncorrectAnswer2());
            incorrectAnswers.add(question.getIncorrectAnswer3());

            tvProgress.setText((questionNum+1) + "/" + numOfQuestions);

            List<String> answerOptions = new ArrayList<>();
            answerOptions.add(correctAnswer);
            answerOptions.addAll(incorrectAnswers);
            Collections.shuffle(answerOptions);

            tvQuestion.setText(questionText);

            btnAnswer1.setText(answerOptions.get(0));
            btnAnswer2.setText(answerOptions.get(1));
            btnAnswer3.setText(answerOptions.get(2));
            btnAnswer4.setText(answerOptions.get(3));

            btnAnswer1.setOnClickListener(e -> {
                GameSounds.clickSound(e.getContext());
                checkAnswer(btnAnswer1.getText().toString(), correctAnswer, false);
            });

            btnAnswer2.setOnClickListener(e -> {
                GameSounds.clickSound(e.getContext());
                checkAnswer(btnAnswer2.getText().toString(), correctAnswer, false);
            });

            btnAnswer3.setOnClickListener(e -> {
                GameSounds.clickSound(e.getContext());
                checkAnswer(btnAnswer3.getText().toString(), correctAnswer, false);
            });

            btnAnswer4.setOnClickListener(e -> {
                GameSounds.clickSound(e.getContext());
                checkAnswer(btnAnswer4.getText().toString(), correctAnswer, false);
            });

            //start our Stopwatch
            startTime = System.currentTimeMillis();

            //Start our countdown
            pbarCountdown.setProgress(0);
            pbarCountdown.setMax(timeLimitPerQuestion);
            timer = new CountDownTimer(timeLimitPerQuestion * 1000, 1000)
            {
                @Override
                public void onTick(long l)
                {
                    pbarCountdown.setProgress(pbarCountdown.getProgress() + 1);
                }

                @Override
                public void onFinish()
                {
                    checkAnswer("", correctAnswer, true);
                }
            }.start();
        }
    }

    private void showRightAnswer(String correctAnswer)
    {
        ArrayList<Button> btns = new ArrayList<>();

        btns.add(btnAnswer1);
        btns.add(btnAnswer2);
        btns.add(btnAnswer3);
        btns.add(btnAnswer4);

        for (Button btn : btns)
        {
            //If it's the right answer, turn it green; if not, turn it red.
            if (btn.getText().toString().equals(correctAnswer))
            {
                btn.setBackgroundColor(Color.GREEN);
                btn.setTextColor(Color.BLACK);
            }
            else
            {
                btn.setBackgroundColor(Color.RED);
                btn.setTextColor(Color.WHITE);
            }
        }
    }

    private void resetBtnColor()
    {
        btnAnswer1.setBackgroundColor(Color.parseColor("#7503A9F4"));
        btnAnswer2.setBackgroundColor(Color.parseColor("#7503A9F4"));
        btnAnswer3.setBackgroundColor(Color.parseColor("#7503A9F4"));
        btnAnswer4.setBackgroundColor(Color.parseColor("#7503A9F4"));

        btnAnswer1.setTextColor(Color.BLACK);
        btnAnswer2.setTextColor(Color.BLACK);
        btnAnswer3.setTextColor(Color.BLACK);
        btnAnswer4.setTextColor(Color.BLACK);
    }

    private void endOfGame()
    {
        String TAG = "EndOfGame";

        //Log our end of game metrics
        Log.d(TAG, "Correct Answers: " + correctAnswerCount);
        Log.d(TAG, "Incorrect Answers; " + (numOfQuestions - correctAnswerCount));

        //Calculate our average answer time
        long milliTotal = 0;

        for (long time : questionTimes)
        {
            Log.d(TAG, "Q-Time: " + time);
            milliTotal += time;
        }

        long avgMilliTime = milliTotal / questionTimes.size();
        int avgSecTime = (int) (avgMilliTime / 1000);

        Log.d(TAG, "Average Time: " + avgSecTime);

        //Prepare the intent to move to the next view
        Intent toSummary = new Intent(this, HistoryActivity.class);

        toSummary.putExtra("category", categoryName);
        toSummary.putExtra("score", (int)((correctAnswerCount / (numOfQuestions + 0.0)) * 100) + "%");
        toSummary.putExtra("correct", correctAnswerCount+"");
        toSummary.putExtra("incorrect", (numOfQuestions - correctAnswerCount)+"");
        toSummary.putExtra("time", avgSecTime+" sec");

        startActivity(toSummary);
        timer.cancel();
        GameSounds.endSound(this);
        this.finish();
    }

    private void checkAnswer(String selectedAnswer, String correctAnswer, boolean timeout)
    {
        //If it's correct; inform the user, increment it, play the appropriate sound.
        if (selectedAnswer.equals(correctAnswer))
        {
            tvQuestion.setText("You got the correct answer");
            correctAnswerCount++;

            GameSounds.correctSound(this);
        }
        // If they ran out of time, they suck. So inform them.
        else if (timeout)
        {
            tvQuestion.setText("You ran out of time");
            GameSounds.vineBoom(this);
        }
        // If we get here, they got it wrong.
        else
        {
            tvQuestion.setText("You got the incorrect answer");
            GameSounds.wrongSound(this);
        }

        //No Matter what we cancel the timer, show the right answer, and move forward.
        timer.cancel();
        showRightAnswer(correctAnswer);

        //Add the time to our log
        endTime = System.currentTimeMillis();
        questionTimes.add(endTime - startTime);

        //Disable the buttons
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);

        //Move to the next questions
        new Handler().postDelayed(() ->
        {
            currentQuestionIndex++;

            resetBtnColor();
            btnAnswer1.setEnabled(true);
            btnAnswer2.setEnabled(true);
            btnAnswer3.setEnabled(true);
            btnAnswer4.setEnabled(true);

            //Figure out if we're at the end of the game
            if (currentQuestionIndex < triviaQuestions.size())
            {
                populateButtonsWithQuestions(triviaQuestions, currentQuestionIndex);
            }
            else
            {
                tvQuestion.setText("You have completed the quiz!");
                currentQuestionIndex = 0;
                endOfGame();
            }
        }, 3000); // 5000 milliseconds (5 seconds) delay

    }

    public List<Question> fetchQuestions() {
        OkHttpClient httpClient = new OkHttpClient();
        List<Question> questionList = new ArrayList<>();

        Runnable fetchQuestionsTask = new Runnable() {
            @Override
            public void run() {
//                String apiUrl = "https://opentdb.com/api.php?amount=" + questionAmount + "&category=" + selectedCategoryId + "&difficulty=" + questionDifficulty + "&type=multiple";
                String apiUrl = "https://opentdb.com/api.php?amount=" + numOfQuestions + "&category=" + categoryID + "&type=multiple";

                Request request = new Request.Builder()
                        .url(apiUrl)
                        .get()
                        .build();

                Call call = httpClient.newCall(request);

                try {
                    Response response = call.execute();

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        List<Question> questions = extractQuestionsAndAnswers(responseBody);

                        runOnUiThread(() -> {
                            if (questions != null && !questions.isEmpty()) {
                                triviaQuestions = questions;
                                Log.d("question", triviaQuestions.toString());
                                populateButtonsWithQuestions(triviaQuestions, 0);
                            } else {
                                // Handle cases where no questions are available
                            }
                        });
                    } else {
                        String errorMessage = "Request failed: " + response.code() + " " + response.message();
                        // Handle the error as needed
                    }
                } catch (IOException e) {
                    String errorMessage = "Network error: " + e.getMessage();
                    // Handle the error as needed
                }
            }
        };

        executorService.execute(fetchQuestionsTask);

        return questionList;
    }

    private List<Question> extractQuestionsAndAnswers(String responseJson) {
        List<Question> questionList = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(responseJson);
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                String question = resultObject.getString("question");
                String correctAnswer = resultObject.getString("correct_answer");
                JSONArray incorrectAnswers = resultObject.getJSONArray("incorrect_answers");

                String incorrectAnswer1 = incorrectAnswers.getString(0);
                String incorrectAnswer2 = incorrectAnswers.getString(1);
                String incorrectAnswer3 = incorrectAnswers.getString(2);

                Question questionObj = new Question(String.valueOf(Html.fromHtml(question)),
                        String.valueOf(Html.fromHtml(correctAnswer)),
                        String.valueOf(Html.fromHtml(incorrectAnswer1)),
                        String.valueOf(Html.fromHtml(incorrectAnswer2)),
                        String.valueOf(Html.fromHtml(incorrectAnswer3)));
                questionList.add(questionObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questionList;
    }
}
