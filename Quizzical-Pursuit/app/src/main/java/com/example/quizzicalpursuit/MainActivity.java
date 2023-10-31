package com.example.quizzicalpursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences TriviaSettings;

    Button btnGame;
    ListView lstCategory;
    ArrayList<Category> category;

    CategoryAdapter adapter;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this); // Initialize the RequestQueue

        lstCategory = findViewById(R.id.lstCategory);
        category = new ArrayList<>();
        adapter  = new CategoryAdapter(category,this);
        lstCategory.setAdapter(adapter);
        getData();


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
