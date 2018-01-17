package com.example.floating.bakingapp.utils

import android.net.Uri

import com.example.floating.bakingapp.model.Ingredients

import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

/**
 * Copyright (c) Abdulkarim Abdulrahman Ayoola on 6/14/2017.
 */

object RecipeUtils {

    val BASE_REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017"
    val JSON_REQUEST_URL = "/May/59121517_baking/baking.json"

    fun buildJsonUrl(): URL? {
        val build = Uri.parse(BASE_REQUEST_URL).buildUpon()
                .appendPath(JSON_REQUEST_URL).build()
        var url: URL? = null
        try {
            url = URL(build.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        return url
    }

    fun setProperMeasure(quantity: Double, measure: String?): String? {
        var measure = measure

        when (measure) {
            "UNIT" -> measure = ""
            "G" -> measure = "gram"
            "K" -> measure = "Kilo"
            "TSP" -> measure = "teaspoon"
            "TBLSP" -> measure = "tablespoon"
            "OZ" -> measure = "ounce"
            "CUP" -> measure = "cup"
        }

        if (quantity > 0) {
            if (quantity > 0 && measure == "")
                measure = quantity.toInt().toString() + " " + measure
            else if (quantity == 0.5)
                measure = "Half $measure of "
            else if (quantity == 1.5)
                measure = "One and half " + measure + "s of "
            else if (quantity == 1.0)
                measure = quantity.toInt().toString() + " " + measure + " of "
            else
                measure = quantity.toInt().toString() + " " + measure + "s of "
        }
        return measure
    }

    fun getIngredientsString(ingredientsList: List<Ingredients>?): String? {
        val ingredientList: String
        val stringBuilder = StringBuilder()

        if (ingredientsList == null)
            return null
        else {
            for (i in ingredientsList.indices) {
                val measures = setProperMeasure(ingredientsList[i].quantity,
                        ingredientsList[i].measure)
                val ingredient = ingredientsList[i].ingredient
                val singleIngredient = measures + ingredient + "\n"
                stringBuilder.append(singleIngredient)
            }

            ingredientList = stringBuilder.toString()
            return ingredientList
        }
    }
}
