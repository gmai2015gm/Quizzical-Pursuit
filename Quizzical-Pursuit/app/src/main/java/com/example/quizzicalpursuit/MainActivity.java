package com.example.quizzicalpursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.*;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    SharedPreferences TriviaSettings;

    Button btnSettings;
    RecyclerView lstCategory;
    ArrayList<Category> category;
    CategoryAdapter adapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener(this));

        queue = Volley.newRequestQueue(this); // Initialize the RequestQueue

        //initialize recycler and buttons
        lstCategory = findViewById(R.id.lstCategory);
        btnSettings = findViewById(R.id.btnSettings);

        //initialize arraylist of categories.
        category = new ArrayList<>();
        //apply category data
        getData();
        //initialize adapter based on the category class
        adapter  = new CategoryAdapter(this,category);
        //set adapter for the recyclerview.
        lstCategory.setAdapter(adapter);

        //apply linearlayour manager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //apply manager
        lstCategory.setLayoutManager(manager);

        //on btn settings being clicked send values and open menu.
        btnSettings.setOnClickListener(e->{
            //get context
            GameSounds.clickSound(e.getContext());
            //create intent
            Intent i = new Intent(this, SettingsActivity.class);
            //send bundle
            Bundle b = new Bundle();
            //put extras and start activity.
            i.putExtras(b);
            startActivity(i);
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                category.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        helper.attachToRecyclerView(lstCategory);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Pull whether or not we're doing music from the preferences
        TriviaSettings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        String sounds = TriviaSettings.getString("sounds", "music, sfx");
        GameSounds.music = sounds.toLowerCase().contains("music");
        GameSounds.sound = sounds.toLowerCase().contains("sfx");
//
//        //Now play the music if we're playing the music.
//        if (GameSounds.music)
//            GameSounds.playMusic(this);
//        else
//            GameSounds.stopMusic();
    }

    @Override
    protected void onPause()
    {
        //Pause the music if we leave.
        super.onPause();
//        GameSounds.pauseMusic();
    }

    // Update this method
    public void getData(){
        String url = "https://opentdb.com/api_category.php";
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        JSONArray categoriesArray = response.getJSONArray("trivia_categories");
                        for(int i = 0; i < categoriesArray.length(); i++){
                            JSONObject categoryObj = categoriesArray.getJSONObject(i);
                            int id = categoryObj.getInt("id");
                            String name = categoryObj.getString("name");
                            Category c = new Category(name, id);
                            category.add(c);
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD",error.toString()));
        queue.add(r);
    }

    /**
     * Custom Listener class enabling wider scope of App Lifetime.
     * Allows monitoring of the foreground/background status of the app
     */
    class AppLifecycleListener implements DefaultLifecycleObserver {
        Context mainContext;

    public AppLifecycleListener(Context context) {
        mainContext = context;
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d("PROCESS_LIFECYCLE", "Starting through new Class");
        DefaultLifecycleObserver.super.onStart(owner);

        TriviaSettings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        String sounds = TriviaSettings.getString("sounds", "music, sfx");
        GameSounds.music = sounds.toLowerCase().contains("music");
        GameSounds.sound = sounds.toLowerCase().contains("sfx");

        if (GameSounds.music)
            GameSounds.playMusic(mainContext);
        else
            GameSounds.stopMusic();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.d("PROCESS_LIFECYCLE", "Stopping through new Class");
        DefaultLifecycleObserver.super.onPause(owner);
        if(GameSounds.music) {
            GameSounds.storeProgress();
            GameSounds.stopMusic();
        }
    }
}
}
