package com.example.floating.bakingapp.utils;

import android.net.Uri;

import com.example.floating.bakingapp.data.Ingredients;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class RecipeUtils {

    public static final String BASE_REQUEST_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017";
    public static final String JSON_REQUEST_URL =
            "/May/59121517_baking/baking.json";

    public static URL buildJsonUrl(){
        Uri build = Uri.parse(BASE_REQUEST_URL).buildUpon()
                .appendPath(JSON_REQUEST_URL).build();
        URL url = null;
        try{
            url = new URL(build.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String setProperMeasure(double quantity, String measure){

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

    public static String getIngredientsString(ArrayList<Ingredients> ingredientsList){
        String ingredientList;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < ingredientsList.size(); i++) {
            String measures = setProperMeasure(ingredientsList.get(i).getQuantity(),
                    ingredientsList.get(i).getMeasure());
            String ingredient = ingredientsList.get(i).getIngredient();
            String singleIngredient = measures + ingredient + "\n";
            stringBuilder.append(singleIngredient);
        }

        ingredientList = stringBuilder.toString();
        return ingredientList;
    }
}
