package com.example.floating.bakingapp.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class Baker {


    private ArrayList<Recipe> recipe;

    public Baker(String bake_json) throws JSONException {
        JSONArray bakeArray = new JSONArray(bake_json);
        this.recipe = new ArrayList<>();
        for (int i = 0; i < bakeArray.length(); i++) {
            recipe.add(new Recipe(bakeArray.getJSONObject(i)));
        }
    }

    public void setRecipe(ArrayList<Recipe> recipe) {
        this.recipe = recipe;
    }

    public ArrayList<Recipe> getRecipe() {
        return recipe;
    }
}
