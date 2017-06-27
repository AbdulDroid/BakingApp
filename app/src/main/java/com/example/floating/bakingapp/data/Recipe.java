package com.example.floating.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class Recipe implements Parcelable {

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {

            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<Ingredients> ingredients;
    @SerializedName("steps")
    private ArrayList<Steps> steps;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    public Recipe() {
    }

    protected Recipe (Parcel in){
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        this.steps = in.createTypedArrayList(Steps.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public Recipe(JSONObject bake_json) throws JSONException {

        this.name = bake_json.getString("name");
        this.ingredients = new ArrayList<>();
        JSONArray ingredientsJA = bake_json.getJSONArray("ingredients");
        for (int i = 0; i < ingredientsJA.length(); i++) {
            ingredients.add(new Ingredients(ingredientsJA.getJSONObject(i)));
        }
        this.steps = new ArrayList<>();
        JSONArray stepsJA = bake_json.getJSONArray("steps");
        for (int i = 0; i < stepsJA.length(); i++) {
            steps.add(new Steps(stepsJA.getJSONObject(i)));
        }
        this.servings = bake_json.getInt("servings");
        this.image = bake_json.getString("image");
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

    public int getServings() {
        return servings;
    }
    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeTypedList(this.ingredients);
        parcel.writeTypedList(this.steps);
        parcel.writeInt(this.servings);
        parcel.writeString(this.image);
    }
}