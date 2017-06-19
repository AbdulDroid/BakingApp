package com.kafilicious.abdul.bakingapp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredients
{

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredients(){}

    public Ingredients(JSONObject ingredient_jason) {
        try {
            this.quantity = ingredient_jason.getDouble("quantity");
            this.measure = ingredient_jason.optString("measure");
            this.ingredient = ingredient_jason.optString("ingredient");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}