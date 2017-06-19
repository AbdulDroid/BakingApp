package com.kafilicious.abdul.bakingapp.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipe
{
    private String name;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Steps> steps;
    private String servings;

    public Recipe(){}

    public Recipe(JSONObject bake_jason) throws JSONException {

        this.name = bake_jason.getString("name");
        this.ingredients = new ArrayList<>();
        JSONArray ingredientsJA = bake_jason.getJSONArray("ingredients");
        for (int i = 0; i < ingredientsJA.length(); i++) {
            ingredients.add(new Ingredients(ingredientsJA.getJSONObject(i)));
        }
        this.steps = new ArrayList<>();
        JSONArray stepsJA = bake_jason.getJSONArray("steps");
        for (int i = 0; i < stepsJA.length(); i++) {
            steps.add(new Steps(stepsJA.getJSONObject(i)));
        }
        this.servings = bake_jason.getString("servings");
    }


    public String getName() {
        return name;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

}