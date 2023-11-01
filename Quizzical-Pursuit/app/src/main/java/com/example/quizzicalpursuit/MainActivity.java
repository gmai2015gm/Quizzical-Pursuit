package com.example.quizzicalpursuit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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

        queue = Volley.newRequestQueue(this); // Initialize the RequestQueue

        lstCategory = findViewById(R.id.lstCategory);
        btnSettings = findViewById(R.id.btnSettings);
        category = new ArrayList<>();
        getData();
        adapter  = new CategoryAdapter(this,category);
        lstCategory.setAdapter(adapter);


        LinearLayoutManager manager = new LinearLayoutManager(this);
        lstCategory.setLayoutManager(manager);

        btnSettings.setOnClickListener(e->{
            Intent i = new Intent(this, SettingsActivity.class);
            Bundle b = new Bundle();

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



}
