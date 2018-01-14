package com.example.floating.bakingapp.recipes;

import com.example.floating.bakingapp.data.ApiService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by apple on 14/01/2018.
 */
@Module
public class RecipeModule {
    RecipeContract.View view;

    RecipeModule(RecipeContract.View view){
        this.view = view;
    }

    @Provides
    @RecipeScope
    RecipeContract.View providesRecipeView(){
        return view;
    }

    @Provides
    @RecipeScope
    RecipePresenter providesRecipePresenter(RecipeContract.View view, ApiService apiService){
        return new RecipePresenter(view, apiService);
    }
}
