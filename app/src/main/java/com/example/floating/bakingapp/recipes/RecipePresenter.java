package com.example.floating.bakingapp.recipes;

import android.support.annotation.NonNull;

import com.example.floating.bakingapp.data.ApiService;
import com.example.floating.bakingapp.model.Baker;
import com.example.floating.bakingapp.model.Recipe;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by apple on 14/01/2018.
 */

public class RecipePresenter implements RecipeContract.Presenter{
    private ApiService apiService;
    private WeakReference<RecipeContract.View> view;

    @Inject
    RecipePresenter(RecipeContract.View view, ApiService apiService) {
        this.view = new WeakReference<>(view);
        this.apiService = apiService;
    }
    @Override
    public void getRecipes() {
        apiService.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                   @NonNull Response<ArrayList<Recipe>> response) {
                if (view == null) return;
                try{
                    if (response.isSuccessful()){
                        Timber.e(response.body().toString());
                        view.get().onGetRecipeSuccess(response.body());
                    } else {
                        Timber.e(response.errorBody().string());
                        view.get().onGetRecipeError("failed to load");
                    }
                } catch (IOException e) {
                    view.get().onGetRecipeError(e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {

            }
        });
    }
}
