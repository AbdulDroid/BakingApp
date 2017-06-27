package com.example.floating.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Recipe;
import com.example.floating.bakingapp.ui.RecipeListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public static List<Recipe> recipe_list;
    private Context context;
    String RECIPE_NAME = "recipe_name";
    String RECIPE_STEPS_LIST = "recipe_steps";
    String RECIPE_LIST = "recipe_list";
    String RECIPE_INDEX = "index";
    public RecipeAdapter(List<Recipe> recipes){
        this.recipe_list = recipes;
    }

    public void steRecipesData(List<Recipe> recipeData){
        this.recipe_list = recipeData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context contextOne = parent.getContext();
        View view;
        view = LayoutInflater.from(contextOne).inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
        String recipeName = null;
        int recipeServing ;
        String recipeImage = null;
        recipeName = recipe_list.get(position).getName();
        recipeServing = recipe_list.get(position).getServings();
        recipeImage = recipe_list.get(position).getImage();

        holder.recipeTextView.setText(recipeName);
        holder.servingTextView.setText(String.format(context.getString(R.string.servings_text), recipeServing));
        Glide.with(context)
                .load(recipeImage)
                .placeholder(imageResource(position))
                .into(holder.recipeImageView);
    }

    @Override
    public int getItemCount() {
        if (recipe_list == null)return 0;
        return recipe_list.size();
    }

    public int imageResource(int index){
        int[] imageResource ={R.drawable.nutella_pie_one, R.drawable.brownies_one,
                R.drawable.yellow_cake_0, R.drawable.cheese_cake_one};
        return imageResource[index];
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_name) TextView recipeTextView;
        @BindView(R.id.number_of_servings) TextView servingTextView;
        @BindView(R.id.recipe_image) ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setClickable(true);
            view.setOnClickListener(this);
            context = view.getContext();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, RecipeListActivity.class);
            intent.putExtra(RECIPE_INDEX, position);
            intent.putParcelableArrayListExtra(RECIPE_LIST, (ArrayList<Recipe>)recipe_list);
            context.startActivity(intent);
        }
    }


}