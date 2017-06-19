package com.kafilicious.abdul.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kafilicious.abdul.bakingapp.R;
import com.kafilicious.abdul.bakingapp.data.Recipe;
import com.kafilicious.abdul.bakingapp.ui.RecipesListActivity;

import java.util.List;

/**
 * Created by Abdulkarim on 6/12/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public static List<Recipe> recipe_list;
    private Context context;
    String RECIPE_NAME = "recipe_name";
    String RECIPE_STEPS_LIST = "recipe_steps";
    String RECIPE_INGREDIENTS_LIST = "recipe_ingredients";
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
        String recipeServing = null;
        recipeName = recipe_list.get(position).getName();
        recipeServing = recipe_list.get(position).getServings();

        holder.recipeTextView.setText(recipeName);
        holder.servingTextView.setText(String.format(context.getString(R.string.servings_text), recipeServing));
        holder.recipeImageView.setImageResource(imageResource(position));
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

        final TextView recipeTextView, servingTextView;
        final ImageView recipeImageView;
        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            recipeTextView = (TextView) view.findViewById(R.id.recipe_name);
            servingTextView = (TextView) view.findViewById(R.id.number_of_servings);
            recipeImageView = (ImageView) view.findViewById(R.id.recipe_image);
            context = view.getContext();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, RecipesListActivity.class);
            intent.putExtra(RECIPE_INDEX, position);
            context.startActivity(intent);
        }
    }
}
