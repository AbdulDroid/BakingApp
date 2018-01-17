package com.example.floating.bakingapp.recipes

import com.example.floating.bakingapp.data.ApiService

import dagger.Module
import dagger.Provides

/**
 * Created by apple on 14/01/2018.
 */
@Module
class RecipeModule internal constructor(internal var view: RecipeContract.View) {

    @Provides
    @RecipeScope
    internal fun providesRecipeView(): RecipeContract.View {
        return view
    }

    @Provides
    @RecipeScope
    internal fun providesRecipePresenter(view: RecipeContract.View, apiService: ApiService): RecipePresenter {
        return RecipePresenter(view, apiService)
    }
}
