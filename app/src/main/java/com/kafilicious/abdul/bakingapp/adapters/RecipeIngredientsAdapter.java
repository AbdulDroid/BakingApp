package com.kafilicious.abdul.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kafilicious.abdul.bakingapp.R;
import com.kafilicious.abdul.bakingapp.data.Ingredients;

import java.util.List;

/**
 * Created by Abdulkarim on 6/16/2017.
 */

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private List<Ingredients> ingredientsList;

    public RecipeIngredientsAdapter(List<Ingredients> ingredientsList){
        this.ingredientsList = ingredientsList;
    }

    public void setIngredientData(List<Ingredients> ingredientData){
        this.ingredientsList = ingredientData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ingredient_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ingredients = ingredientsList.get(position);
        String measure = "";
        switch (ingredientsList.get(position).getMeasure()){
            case "UNIT":
                measure = "";
                break;
            case "G":
                measure = "gram";
                break;
            case "K":
                measure = "Kilo";
                break;
            case "TSP":
                measure = "teaspoon";
                break;
            case "TBLSP":
                measure = "tablespoon";
                break;
            case "OZ":
                measure = "ounce";
                break;
            case "CUP":
                measure = "cup";
                break;
        }
        double quantity = ingredientsList.get(position).getQuantity();
        String ingredient = ingredientsList.get(position).getIngredient();
        if (quantity > 1)
            measure = measure + "s";

        holder.ingredientTextView.setText(quantity + " " + measure + " of " + ingredient );


    }

    @Override
    public int getItemCount() {
        if (ingredientsList == null)return 0;
        return ingredientsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ingredientTextView;
        public Ingredients ingredients;
        public ViewHolder(View view) {
            super(view);
            view.setClickable(false);
            ingredientTextView = (TextView) view.findViewById(R.id.ingredient);
        }
    }
}
