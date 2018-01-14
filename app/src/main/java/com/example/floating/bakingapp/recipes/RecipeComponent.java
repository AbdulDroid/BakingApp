package com.example.floating.bakingapp.recipes;

import dagger.Subcomponent;

/**
 * Created by apple on 14/01/2018.
 */
@RecipeScope
@Subcomponent(modules = RecipeModule.class)
public interface RecipeComponent {
    void inject(RecipeActivity recipeActivity);
}
