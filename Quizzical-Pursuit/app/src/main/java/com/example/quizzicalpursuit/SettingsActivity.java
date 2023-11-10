package com.example.quizzicalpursuit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    ActivityResultLauncher resultLauncher;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SeekBar sbNumQuest, sbTimePerQuest;
    TextView txtNumQuest, txtTimePerQuest;
    Button btnMusic, btnSFX, btnSettingsSP, btnCategoriesSP, btnSave, btnCancel;
    ImageButton ibInsta;
    String TAG = "SETTINGS";

    // This array list will hold the strings "Music" and "SFX" which will be sent through preferences
    // When user clicks music and sfx button on/off it will be added/removed from list
    ArrayList<String> sounds;

    // Variables to hold the number of questions and the time per question that user sets with seek bar
    int numQuestions;
    int timePerQuestion;

    // Boolean for if sound (music and sfx) is on or not. Will turned true or false based on button user clicks
    // Used to check which background color and text button has as well as if sound is on
    boolean music = false;
    boolean sfx = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the preferences and editor
        preferences = getSharedPreferences(TAG, MODE_PRIVATE);
        editor = preferences.edit();

        // Get all the activity elements
        btnMusic = findViewById(R.id.btnMusic);
        btnSFX = findViewById(R.id.btnSFX);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnSettingsSP = findViewById(R.id.btnSettingsSP);
        btnCategoriesSP = findViewById(R.id.btnCategoriesSP);
        ibInsta = findViewById(R.id.ibInsta);
        sbNumQuest = findViewById(R.id.sbNumQuest);
        sbTimePerQuest = findViewById(R.id.sbTimePerQuest);
        txtNumQuest = findViewById(R.id.txtNumQuest);
        txtTimePerQuest = findViewById(R.id.txtTimePerQuest);

        // Set background color for the buttons initially
        btnSave.setBackgroundColor(Color.parseColor("#FF4CAF50"));
        btnCancel.setBackgroundColor(Color.parseColor("#F64C4C"));

        // Get the preferences for if user wants music or sfx
        String[] sound = preferences.getString("sounds", "Music, SFX").split(",");

        // Get the number of questions they want and amount of time per question
        numQuestions = preferences.getInt("numQuestions", 10);
        timePerQuestion = preferences.getInt("timePerQuestion", 30);

        // Initialize the array list for sounds
        sounds = new ArrayList<>();
        List<String> s = Arrays.asList(sound);

        // Show if user initially wanted music
        // Change text and background to match
        if (s.contains("Music")) {
            btnMusic.setBackgroundColor(Color.parseColor("#FF4CAF50"));
            btnMusic.setText("On");
            sounds.add("Music");
            music = true;

            // Start the music
            GameSounds.music = true;
            GameSounds.playMusic(this);
        } else {
            music = false;
            btnMusic.setText("Off");
            btnMusic.setBackgroundColor(Color.parseColor("#F64C4C"));
        }

        // Show if user initially wanted sfx
        // Change text and background to match
        if (s.contains("SFX")) {
            btnSFX.setBackgroundColor(Color.parseColor("#FF4CAF50"));
            btnSFX.setText("On");
            sounds.add("SFX");

            // Make boolean sound from Game sounds true (sfx will work)
            GameSounds.sound = true;
            sfx = true;
        } else {
            sfx = false;
            btnSFX.setText("Off");
            btnSFX.setBackgroundColor(Color.parseColor("#F64C4C"));
        }

        // Progress Bar implementation Logic for time per question
        sbTimePerQuest.setProgress(timePerQuestion);
        txtTimePerQuest.setText(sbTimePerQuest.getProgress() + "");
        sbTimePerQuest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtTimePerQuest.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Progress Bar implementation Logic for number of questions
        sbNumQuest.setProgress(numQuestions);
        txtNumQuest.setText(sbNumQuest.getProgress() + "");
        sbNumQuest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtNumQuest.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Each time a button is clicked they will be added of removed from the sounds array
        btnMusic.setOnClickListener(e -> {
            // Check if button is supposed to be "on"
            // Change text and background to match
            if (!music) {
                music = true;
                btnMusic.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                btnMusic.setText("On");
                sounds.add("Music");

                // Play music
                GameSounds.music = true;
                GameSounds.playMusic(this);
            } else {
                music = false;
                btnMusic.setBackgroundColor(Color.parseColor("#F64C4C"));
                btnMusic.setText("Off");

                // Pause music
                sounds.remove("Music");
                GameSounds.pauseMusic();
            }

            // Play sfx if sound is true
            if (GameSounds.sound) {
                GameSounds.clickSound(this);
            }
        });

        // Each time a button is clicked they will be added of removed from the sounds array
        btnSFX.setOnClickListener(e -> {
            // Check if button is supposed to be "on"
            // Change text and background to match
            if (!sfx) {
                sfx = true;
                btnSFX.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                btnSFX.setText("On");
                sounds.add("SFX");

                // Make sound true so that sfx works
                GameSounds.sound = true;
            } else {
                sfx = false;
                btnSFX.setBackgroundColor(Color.parseColor("#F64C4C"));
                btnSFX.setText("Off");
                sounds.remove("SFX");

                // Make sound false so that sfx don't play
                GameSounds.sound = false;
            }

            // If sound is true, play click sfx
            if (GameSounds.sound) {
                GameSounds.clickSound(this);
            }
        });

        // When this button is clicked, preferences will be saved
        btnSave.setOnClickListener(e -> {
            // Make sound type string to send through preferences
            String sendSound = "";
            for (int j = 0; j < sounds.size(); j++) {
                sendSound += sounds.get(j) + ",";
            }

            // Will play the click sound of sound is true
            if (GameSounds.sound) {
                GameSounds.clickSound(this);
            }

            // Edit the numQuestions, timePerQuestion, question type, and sounds in preferences
            editor.putInt("numQuestions", Integer.parseInt(txtNumQuest.getText() + ""));
            editor.putInt("timePerQuestion", Integer.parseInt(txtTimePerQuest.getText() + ""));
            editor.putString("sounds", sendSound);
            editor.apply();

            // Display message for user when things are saved correctly
            Toast.makeText(this, "Changes saved successfully.", Toast.LENGTH_LONG).show();

        });

        // Will send user to Settings again
        // If user has not saved the settings, they will be reset when cancel button is clicked
        btnCancel.setOnClickListener(e -> {
            // If sound id true, play click sfx
            if (GameSounds.sound) {
                GameSounds.clickSound(this);
            }

            Intent i = new Intent(this, SettingsActivity.class);

            // Display message for user when things are saved correctly
            Toast.makeText(this, "Changes Deleted.", Toast.LENGTH_LONG).show();
            resultLauncher.launch(i);
        });

        // Implicit intent to instagram page for Quizzical pursuit
        ibInsta.setOnClickListener(e -> {
            Uri uri = Uri.parse("https://www.instagram.com/pursuitquizzical");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        });

        // Will send user to Main activity that shows categories
        btnCategoriesSP.setOnClickListener(e -> {
            // If sound is true, play click sfx
            if (GameSounds.sound) {
                GameSounds.clickSound(this);
            }
            Intent i = new Intent(this, MainActivity.class);
            resultLauncher.launch(i);
        });


        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.d("Main_Activity", "Activity was finished.");
            Log.d("Main_Activity", result.getResultCode() + "");
        });
    }
}