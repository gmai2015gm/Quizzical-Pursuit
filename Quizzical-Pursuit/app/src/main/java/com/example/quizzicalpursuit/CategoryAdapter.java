package com.example.quizzicalpursuit;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Category> category;
    Context context;
    int pos;

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
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        pos = position;
        Category c = category.get(position);
        holder.txtName.setText(""+c.name);
    }

    @Override
    public int getItemCount() {
        return category.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        Button btnSelect;

        public CategoryViewHolder(@NonNull View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);
            btnSelect = view.findViewById(R.id.btnSelect);
            btnSelect.setBackgroundColor(Color.parseColor("#FF4CAF50"));


            btnSelect.setOnClickListener(e->{
                Log.d("HESH","SPAM "+ txtName.getText());

                pos = getAdapterPosition();

                Intent i = new Intent(view.getContext(), GameActivity.class);
                Bundle b = new Bundle();

                b.putString("CAT",""+txtName.getText());
                b.putInt("CATID", category.get(pos).getId());
                i.putExtras(b);
                startActivity(view.getContext(),i,b);

                GameSounds.vineBoom(e.getContext());
            });

        }


    }
}
