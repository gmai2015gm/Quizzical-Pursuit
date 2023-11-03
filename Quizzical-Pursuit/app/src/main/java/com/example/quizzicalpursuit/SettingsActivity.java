package com.example.quizzicalpursuit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    ActivityResultLauncher resultLauncher;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SeekBar sbNumQuest, sbTimePerQuest;
    TextView txtNumQuest, txtTimePerQuest;
    CheckBox cbMC, cbTF;
    Button btnMusic, btnSFX, btnSettingsSP, btnCategoriesSP, btnSave;
    String TAG = "SETTINGS";
    ArrayList<String> questionType;
    ArrayList<String> sounds;

    // Variables to hold the number of questions and the time per question
    int numQuestions;
    int timePerQuestion;

    // Boolean for if sound (music and sfx) is on or not
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
        btnSettingsSP = findViewById(R.id.btnSettingsSP);
        btnCategoriesSP = findViewById(R.id.btnCategoriesSP);
        sbNumQuest = findViewById(R.id.sbNumQuest);
        sbTimePerQuest = findViewById(R.id.sbTimePerQuest);
        txtNumQuest = findViewById(R.id.txtNumQuest);
        txtTimePerQuest = findViewById(R.id.txtTimePerQuest);
        cbTF = findViewById(R.id.cbTF);
        cbMC = findViewById(R.id.cbMC);

        // Set background color for the buttons initially
        btnMusic.setBackgroundColor(Color.RED);
        btnSFX.setBackgroundColor(Color.RED);
        btnSave.setBackgroundColor(Color.parseColor("#FF4CAF50"));

        // Get the preferences for what types of questions that user wants
        String[] types = preferences.getString("questionType", "Multiple Choice, True or False").split(",");

        // Get the preferences for if user wants music or sfx
        String[] sound = preferences.getString("sounds", "Music, SFX").split(",");

        // Get the number of questions they want and amount of time per question
        numQuestions = preferences.getInt("numQuestions", 10);
        timePerQuestion = preferences.getInt("timePerQuestion", 30);

        // Initialize the array list for question type and sounds
        questionType = new ArrayList<>();
        sounds = new ArrayList<>();

        // Check initial boxes to see what should be checked
        for (String s : types) {
            if (s.contains("Multiple Choice")) {
                cbMC.setChecked(true);
                questionType.add("Multiple Choice");
            }
            if (s.contains("True or False")) {
                cbTF.setChecked(true);
                questionType.add("True or False");
            }
        }

        // Show if user initially wanted music or sfx
        // Change text and background to match
        for (String s : sound) {
            if (s.contains("Music")) {
                btnMusic.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                btnMusic.setText("On");
                sounds.add("Music");
                music = true;
            }
            if (s.contains("SFX")) {
                btnSFX.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                btnMusic.setText("On");
                sounds.add("SFX");
                sfx = true;
            }
        }

        // When this button is clicked, preferences will be saved
        btnSave.setOnClickListener(e -> {
            // Make sure user checks at least one type of question
            if (questionType.size() >= 1) {

                // Make question type string to send through preferences
                String sendQuest = "";
                for(int j=0; j<questionType.size();j++){
                    sendQuest += questionType.get(j) + ",";
                }

                // Make sound type string to send through preferences
                String sendSound = "";
                for(int j=0; j<sounds.size();j++){
                    sendSound += sounds.get(j) + ",";
                }

                // Edit the numQuestions, timePerQuestion, question type, and sounds in preferences
                editor.putInt("numQuestions", Integer.parseInt(txtNumQuest.getText() +""));
                editor.putInt("timePerQuestion", Integer.parseInt(txtTimePerQuest.getText() + ""));
                editor.putString("questionType", sendQuest);
                editor.putString("sounds", sendSound);
                editor.apply();

                // To check if things are saving correctly
                Log.d(TAG, "numQuestions" + preferences.getInt("numQuestions", 1));
                Log.d(TAG, "timePerQuestion" + preferences.getInt("timePerQuestion", 1));
                Log.d(TAG, "questionType" + preferences.getString("questionType", "1"));
                Log.d(TAG, "sounds" + preferences.getString("sounds", "1"));

                // Display message for user when things are saved correctly
                Toast.makeText(this, "Changes saved successfully.", Toast.LENGTH_LONG).show();
            } else {
                // Display message for user because they didn't click enough types
                Toast.makeText(this, "Please select at least two options.", Toast.LENGTH_LONG).show();
            }

        });


        // Each time a button is clicked they will be added of removed from the sounds array
        btnMusic.setOnClickListener(e ->{
            // Check if button is supposed to be "on"
            // Change text and background to match
            if (!music) {
                music = true;
                btnMusic.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                btnMusic.setText("On");
                sounds.add("Music");
            } else if (music) {
                music = false;
                btnMusic.setBackgroundColor(Color.RED);
                btnMusic.setText("Off");
                sounds.remove("Music");
            }
        });

        // Each time a button is clicked they will be added of removed from the sounds array
        btnSFX.setOnClickListener(e ->{
            // Check if button is supposed to be "on"
            // Change text and background to match
            if (!sfx) {
                sfx = true;
                btnSFX.setBackgroundColor(Color.parseColor("#FF4CAF50"));
                btnSFX.setText("On");
                sounds.add("SFX");
            } else if (sfx) {
                sfx = false;
                btnSFX.setBackgroundColor(Color.RED);
                btnSFX.setText("Off");
                sounds.remove("SFX");
            }
        });

        // Progress Bar implementation Logic for time per question
        sbTimePerQuest.setProgress(timePerQuestion);
        txtTimePerQuest.setText(sbTimePerQuest.getProgress() + "");
        sbTimePerQuest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtTimePerQuest.setText(i + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Will send user to Main activity
        btnCategoriesSP.setOnClickListener(e -> {
            Intent i = new Intent(this, MainActivity.class);
            resultLauncher.launch(i);
        });

        // Will send user to Settings again if they wish to
        btnSettingsSP.setOnClickListener(e ->{
            Intent i = new Intent(this, SettingsActivity.class);
            resultLauncher.launch(i);
        });


        // Initialize the resultLauncher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            Log.d("Main_Activity","Activity was finished.");
            Log.d("Main_Activity",result.getResultCode()+"");
            switch (result.getResultCode()){
                case 0:Log.d("Main_Activity","Back Button was pressed");break;
                case 222:Log.d("Main_Activity","Useractivity finished");break;
                case 333:Log.d("Main_Activity","Signup activity finished. Returned "+result.getData().getStringExtra("name"));break;
            }
        });
    }


    // Based on what boxes are checked or not, add and remove from array
    public void checkActions(View v){
        CheckBox chkSelected = (CheckBox) v;

        // Make sure there are not repeats
        if (questionType.contains(chkSelected.getText())){
            questionType.remove(chkSelected.getText().toString());
            questionType.remove(chkSelected.getText().toString());
        }

        // Add and remove
        if(chkSelected.isChecked()) {
            questionType.add(chkSelected.getText().toString());
        }else {
            questionType.remove(chkSelected.getText().toString());
        }

    }
}