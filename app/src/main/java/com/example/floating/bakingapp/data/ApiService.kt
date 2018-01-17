package com.example.floating.bakingapp.data

import com.example.floating.bakingapp.model.Recipe

import java.util.ArrayList

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Abdulrahman on 11/01/2018.
 */

interface ApiService {

    @get:GET("topher/2017/May/59121517_baking/baking.json")
    val recipes: Call<ArrayList<Recipe>>

    companion object {
        val BASE_URL = "https://d17h27t6h515a5.cloudfront.net/"
    }

}
