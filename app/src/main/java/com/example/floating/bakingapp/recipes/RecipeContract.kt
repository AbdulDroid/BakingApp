package com.example.floating.bakingapp.recipes

import com.example.floating.bakingapp.model.Recipe

import java.util.ArrayList

/**
 * Created by apple on 14/01/2018.
 */

internal interface RecipeContract {

    interface View {
        fun onGetRecipeSuccess(recipes: ArrayList<Recipe>)
        fun onGetRecipeError(error: String)
    }

    interface Presenter {
        fun getRecipes()
    }
}
