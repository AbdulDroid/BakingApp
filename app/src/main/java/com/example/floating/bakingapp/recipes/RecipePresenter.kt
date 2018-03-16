package com.example.floating.bakingapp.recipes

import com.example.floating.bakingapp.data.ApiService
import com.example.floating.bakingapp.model.Baker
import com.example.floating.bakingapp.model.Recipe
import com.google.gson.annotations.Expose

import java.io.IOException
import java.lang.ref.WeakReference
import java.util.ArrayList

import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Created by apple on 14/01/2018.
 */

class RecipePresenter @Inject
internal constructor(view: RecipeContract.View, private val apiService: ApiService) : RecipeContract.Presenter {
    private val view: WeakReference<RecipeContract.View>?

    init {
        this.view = WeakReference(view)
    }

    override fun getRecipes() {
        apiService.recipes.enqueue(object : Callback<ArrayList<Recipe>> {
            override fun onResponse(call: Call<ArrayList<Recipe>>,
                                    response: Response<ArrayList<Recipe>>) {
                if (view == null) return
                try {
                    if (response.isSuccessful) {
                        Timber.e(response.body()!!.toString())
                        view.get()!!.onGetRecipeSuccess(response.body()!!)
                    } else {
                        Timber.e(response.errorBody()!!.string())
                        view.get()!!.onGetRecipeError("failed to load")
                    }
                } catch (e: IOException) {
                    view.get()!!.onGetRecipeError(e.message!!)
                }

            }

            override fun onFailure(call: Call<ArrayList<Recipe>>, t: Throwable) {

            }
        })
    }
}
