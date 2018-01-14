package com.example.floating.bakingapp.recipes;

import com.example.floating.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Created by apple on 14/01/2018.
 */

public interface RecipeContract {

    interface View {
        void onGetRecipeSuccess(ArrayList<Recipe> recipes);
        void onGetRecipeError(String error);
    }

    interface Presenter{
        void getRecipes();
    }
}
