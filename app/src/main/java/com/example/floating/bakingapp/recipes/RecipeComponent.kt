package com.example.floating.bakingapp.recipes

import dagger.Subcomponent

/**
 * Created by apple on 14/01/2018.
 */
@RecipeScope
@Subcomponent(modules = [(RecipeModule::class)])
interface RecipeComponent {
    fun inject(recipeActivity: RecipeActivity)
}
