package com.example.quizzicalpursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    TextView txtCategory;
    TextView txtScore;
    TextView txtTotalCorrect;
    TextView txtTotalIncorrect;
    TextView txtAvgTime;
    Button btnCategories;
    Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        txtCategory = findViewById(R.id.txtCategory);
        txtScore = findViewById(R.id.txtScore);
        txtTotalCorrect = findViewById(R.id.txtTotalCorrect);
        txtTotalIncorrect = findViewById(R.id.txtTotalIncorrect);
        txtAvgTime = findViewById(R.id.txtAvgTime);

        btnCategories = findViewById(R.id.btnCategories);
        btnSettings = findViewById(R.id.btnSettings);

        //\\ ---<<< TEXTVIEW POPULATION >>>--- //\\

        // Get game results from intent
        Intent i = getIntent();

        // Populate TextViews from intent

        txtCategory.setText(i.getStringExtra("category"));
        txtScore.setText(i.getStringExtra("score"));
        txtTotalCorrect.setText(i.getStringExtra("correct"));
        txtTotalIncorrect.setText(i.getStringExtra("incorrect"));
        txtAvgTime.setText(i.getStringExtra("time"));



        //\\ ---<<< ACTIVITY REDIRECTS >>>--- //\\

        // Redirect Player to the Main Menu (Category Selection Screen)
        btnCategories.setOnClickListener(v -> {
            Intent redirect = new Intent(this, MainActivity.class);
            startActivity(redirect);
        });

        // Redirect the player to the Settings
        btnSettings.setOnClickListener(v -> {
            Intent redirect = new Intent(this, SettingsActivity.class);
            startActivity(redirect);
        });
    }
}