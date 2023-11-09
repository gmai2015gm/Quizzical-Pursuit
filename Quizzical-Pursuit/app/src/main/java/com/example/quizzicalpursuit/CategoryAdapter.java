package com.example.quizzicalpursuit;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    //initialize arraylist, context, pos - used to get the current int ID from the website end, and sharedpreferences.
    ArrayList<Category> category;
    ArrayList<Integer> faveList;
    Context context;
    int pos;
    SharedPreferences sp;


    public CategoryAdapter(Context context,ArrayList<Category> category) {
        this.category = category;
        this.context = context;

        //initialize arraylist of favorites.
        faveList = new ArrayList<>();
        //apply values for sharedpreferences.
        sp = context.getSharedPreferences("FAVES", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category_layout,parent,false);

        // Populate Favorites List
        getFavorites();

        // Sort Categories for intial list population
        sortCategories();

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        pos = position;

        //get category position and set text name
        Category c = category.get(position);
        holder.txtName.setText(""+c.name);

        getFavorites();

        //apply favorite image resources.
        if(faveList.contains(category.get(position).getId()))
            holder.btnFav.setImageResource(R.drawable.h1);
        else
            holder.btnFav.setImageResource(R.drawable.h2);

    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    /**
     * Populate stored favorites list from SharedPreferences
     */
    private void getFavorites() {
        try{
            String keeper = sp.getString("fa"," ");
            String holderArray[] = keeper.split(" ");
            for(int k = 0;k<holderArray.length;k++){
                faveList.add(Integer.parseInt(holderArray[k]));
            }

            HashSet<Integer> set = new HashSet<>();
            List<Integer> result = new ArrayList<>();

            for (Integer item : faveList) {
                if (set.add(item)) {
                    result.add(item);
                }
            }

            faveList.clear();
            faveList.addAll(result);

        }catch(Exception e){
            //Log.d("HESH",e+"");
        }
    }

    /**
     * Sort Categories list with favorites on top.
     * Secondary sorting performed w/ category ID
     */
    private void sortCategories() {
        category.sort((c1, t1) -> {
            if(faveList.contains(c1.getId()) && !faveList.contains(t1.getId()))
                return -1;
            else if(!faveList.contains(c1.getId()) && faveList.contains(t1.getId()))
                return 1;
            return c1.getId() - t1.getId();
        });
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{

        //initialize name, button for favorites, and button for selecting items.
        TextView txtName;
        Button btnSelect;
        ImageButton btnFav;
        public CategoryViewHolder(@NonNull View view) {
            super(view);

            //intialize local buttons for recyclerview.
            txtName = view.findViewById(R.id.txtName);
            btnSelect = view.findViewById(R.id.btnSelect);
            btnFav = view.findViewById(R.id.btnFav);
//            faveList = new ArrayList<>();


//            sp = view.getContext().getSharedPreferences("FAVES", Context.MODE_PRIVATE);

            btnSelect.setBackgroundColor(Color.parseColor("#FF4CAF50"));

            //on btnselect being clicked perform the following.
            btnSelect.setOnClickListener(e->{
                Log.d("HESH","SPAM "+ txtName.getText());

                //set position value
                pos = getAdapterPosition();

                //create intent and generate bundle
                Intent i = new Intent(view.getContext(), GameActivity.class);
                Bundle b = new Bundle();

                //send CATegory and CATegoryID to gameactivity.
                b.putString("CAT",""+txtName.getText());
                b.putInt("CATID", category.get(pos).getId());

                //apply bundle and send values.
                i.putExtras(b);
                startActivity(view.getContext(),i,b);

                GameSounds.startSound(e.getContext());
            });

            //on btnFav.click
            btnFav.setOnClickListener(e->{

                //check if the item is already in the favelist and add
                if(!faveList.contains(category.get(getAdapterPosition()).getId())){
                    faveList.add(category.get(getAdapterPosition()).getId());
                    //btnFav.setImageResource(R.drawable.h1);

                    //else remove from favelist.
                }else{

                    faveList.remove(faveList.indexOf(category.get(getAdapterPosition()).getId()));
                    btnFav.setImageResource(R.drawable.h2);
                }

                //apply button picture for items in the favelist.
                if(faveList.contains(category.get(getAdapterPosition()).getId())){
                    btnFav.setImageResource(R.drawable.h1);
                }

                //create sharedpreference editor.
                SharedPreferences.Editor ed = sp.edit();

                //initialize favorite items holder
                String fav = "";
                //loop and apply values for the favelist. space delimited.
                for(int j = 0;j<faveList.size();j++){
                    fav += faveList.get(j).toString() + " ";
                }
                // ReSort categories whenever new favorite is added
                sortCategories();
                notifyDataSetChanged();

                //place fav in sharedpreferences. apply.
                ed.putString("fa",fav);
                ed.apply();

//                Log.d("HESH",category.get(getAdapterPosition()).getId()+" "+ faveList.toString());
            });

        }


    }
}
