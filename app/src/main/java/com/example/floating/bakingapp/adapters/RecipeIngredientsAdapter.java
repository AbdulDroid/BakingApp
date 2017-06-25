package com.example.floating.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.floating.bakingapp.R;
import com.example.floating.bakingapp.data.Ingredients;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.floating.bakingapp.R.id.ingredient;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private List<Ingredients> ingredientsList;
    private Context context;

    public RecipeIngredientsAdapter(List<Ingredients> ingredientsList){
        this.ingredientsList = ingredientsList;
    }

    public void setIngredientData(List<Ingredients> ingredientData){
        this.ingredientsList = ingredientData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ingredient_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ingredients = ingredientsList.get(position);
        String measure = "";


//        switch (ingredientsList.get(position).getMeasure()){
//            case "UNIT":
//                measure = "";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//            case "G":
//                measure = "gram";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//            case "K":
//                measure = "Kilo";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//            case "TSP":
//                measure = "teaspoon";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//            case "TBLSP":
//                measure = "tablespoon";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//            case "OZ":
//                measure = "ounce";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//            case "CUP":
//                measure = "cup";
//                measure = setProperMeasure(ingredientsList.get(position).getQuantity(), measure);
//                break;
//        }

        measure = setProperMeasure(ingredientsList.get(position).getQuantity(),
                ingredientsList.get(position).getMeasure());
        String ingredient = ingredientsList.get(position).getIngredient();


        holder.ingredientTextView.setText(measure + ingredient + "");


    }

    @Override
    public int getItemCount() {
        if (ingredientsList == null)return 0;
        return ingredientsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(ingredient) TextView ingredientTextView;
        public Ingredients ingredients;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(false);
            ButterKnife.bind(this, view);
        }
    }

    public String setProperMeasure(double quantity, String measure){

        switch (measure){
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

        if (quantity > 0){
            if (quantity > 0 && measure.equals(""))
                measure = (int) quantity + " " + measure;
            else if (quantity == 0.5)
                measure = "Half " + measure + " of ";
            else if (quantity == 1.5)
                measure = "One and half " + measure + "s of ";
            else if (quantity == 1.0)
                measure = (int) quantity + " " + measure + " of ";
            else
                measure = (int) quantity + " " + measure + "s of ";
        }
        return measure;
    }

    String getIngredientsString(String measures, String ingredient){
        String ingredientList;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < ingredientsList.size(); i++) {
            measures = setProperMeasure(ingredientsList.get(i).getQuantity(),
                    ingredientsList.get(i).getMeasure());
            ingredient = ingredientsList.get(i).getIngredient();
            String singleIngredient = ". " + measures + ingredient + "\n";
            stringBuilder.append(singleIngredient);
        }

        ingredientList = stringBuilder.toString();
        return ingredientList;
    }
}
