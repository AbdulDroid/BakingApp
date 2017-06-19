package com.kafilicious.abdul.bakingapp.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Abdulkarim on 6/16/2017.
 */

public class Baker {


    private ArrayList<Recipe> recipe;

    public Baker(String bake_jason) throws JSONException {
        JSONArray bakeArray = new JSONArray(bake_jason);
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
