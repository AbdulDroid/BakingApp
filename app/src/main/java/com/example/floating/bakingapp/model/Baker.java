package com.example.floating.bakingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

public class Baker {
    @SerializedName("id")
    @Expose
    long id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("ingredients")
    @Expose
    List<Ingredients> ingredients;
    @SerializedName("steps")
    @Expose
    List<Steps> steps;
    @SerializedName("servings")
    @Expose
    String servings;
    @SerializedName("image")
    @Expose
    String image;

    public Baker() {
    }

    public Baker(long id, String name, List<Ingredients> ingredients, List<Steps> steps,
                 String servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Baker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings='" + servings + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
