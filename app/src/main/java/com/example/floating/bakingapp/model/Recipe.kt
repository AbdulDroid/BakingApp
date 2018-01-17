package com.example.floating.bakingapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.example.floating.bakingapp.R.string.ingredients
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel


/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */
@SuppressLint("ParcelCreator")
@Parcel
data class Recipe(var id: Long) {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("ingredients")
    @Expose
    var ingredients: List<Ingredients>? = null
    @SerializedName("steps")
    @Expose
    val steps: List<Steps>? = null
    @SerializedName("servings")
    @Expose
    val servings: Int = 0
    @SerializedName("image")
    @Expose
    val image: String? = null
    @SerializedName("ingredient_string")
    @Expose
    var ingredient_string: String? = null


    override fun toString(): String {
        return "Recipe{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                ", ingredient_string='" + ingredient_string + '\'' +
                '}'
    }
}