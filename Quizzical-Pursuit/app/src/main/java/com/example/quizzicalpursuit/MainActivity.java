package com.example.quizzicalpursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    SharedPreferences TriviaSettings;

    Button btnGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGame = findViewById(R.id.btnGame);

        btnGame.setOnClickListener(e -> {
            // Create an Intent to start the GameActivity
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

    }

}
