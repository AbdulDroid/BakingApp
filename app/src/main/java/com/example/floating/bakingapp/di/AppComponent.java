package com.example.floating.bakingapp.di;

import com.example.floating.bakingapp.recipes.RecipeComponent;
import com.example.floating.bakingapp.recipes.RecipeModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by apple on 14/01/2018.
 */
@Singleton
@Component (modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    RecipeComponent add(RecipeModule recipeModule);
}
