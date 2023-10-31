package com.example.quizzicalpursuit;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    ArrayList<Category> category;
    Context context;

    public CategoryAdapter(ArrayList<Category> category, Context context) {
        this.category = category;
        this.context = context;
    }


    @Override
    public int getCount() {
        return category.size();
    }

    @Override
    public Object getItem(int i) {
        return category.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.category_layout,parent,false);
        TextView txtName = view.findViewById(R.id.txtName);
        Button btnSelect = view.findViewById(R.id.btnSelect);


        Category c = category.get(i);
        txtName.setText(c.name);

        btnSelect.setOnClickListener(e->{
            // Create an Intent to start the GameActivity
            Intent intent = new Intent(context, GameActivity.class);

            // Get the selected category's ID
            int categoryId = category.get(i).id;

            // Store the category ID in SharedPreferences
            SharedPreferences TriviaPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = TriviaPreferences.edit();
            editor.putInt("selectedCategoryId", categoryId);
            editor.apply();

            context.startActivity(intent);

        });



        return view;
    }
}
