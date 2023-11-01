package com.example.quizzicalpursuit;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Category> category;
    Context context;

    public CategoryAdapter(Context context,ArrayList<Category> category) {
        this.category = category;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_layout,parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c = category.get(position);
        holder.txtName.setText("Name :"+c.name);
    }

    @Override
    public int getItemCount() {
        return category.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;

        public CategoryViewHolder(@NonNull View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);


        }


    }
}
